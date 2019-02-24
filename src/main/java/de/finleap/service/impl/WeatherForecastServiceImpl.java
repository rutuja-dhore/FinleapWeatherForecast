package de.finleap.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Service;

import de.finleap.Util.OpenWeatherMapOrgHttpUtil;
import de.finleap.dto.AverageWeatherForecastDTO;
import de.finleap.exception.ErrorCode;
import de.finleap.exception.ValidationException;
import de.finleap.model.WeatherForecast;
import de.finleap.model.WeatherForecastList;
import de.finleap.service.WeatherForecastService;
import net.iakovlev.timeshape.TimeZoneEngine;

@Service
public class WeatherForecastServiceImpl implements WeatherForecastService {

	private static final double KELVIN_CONSTANT = 273.15;

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static final TimeZoneEngine engine = TimeZoneEngine.initialize();

	@Override
	public AverageWeatherForecastDTO getAverageWeatherForcastByCity(@NotEmpty String city) throws IOException {

		WeatherForecast weatherForecast = OpenWeatherMapOrgHttpUtil.getWeatherForcastByCity(city);
		if (null == weatherForecast) {
			throw new ValidationException(ErrorCode.SOMETHING_WENT_WRONG);
		}

		// Get zoneId of specified city.
		Optional<ZoneId> cityZoneId = engine.query(weatherForecast.getCity().getCoord().getLat(),
				weatherForecast.getCity().getCoord().getLon());

		ZonedDateTime nowInCityZoneId = null;
		if (cityZoneId.isPresent()) {
			nowInCityZoneId = ZonedDateTime.now(cityZoneId.get());
		} else {
			throw new ValidationException(ErrorCode.VALIDATION_INVALID_ZONE_ID);
		}

		if (nowInCityZoneId == null) {
			throw new ValidationException(ErrorCode.VALIDATION_INVALID_ZONE_ID);
		}
		LocalDate localDatePlus3 = nowInCityZoneId.toLocalDate().plusDays(3);

		List<WeatherForecastList> dayTimeForecasts = new ArrayList<>();
		List<WeatherForecastList> nightTimeForecasts = new ArrayList<>();
		List<WeatherForecastList> presureTimeForecasts = new ArrayList<>();

		LocalTime startlocalTime = LocalTime.of(06, 00, 00);
		LocalTime endlocalTime = LocalTime.of(18, 00, 00);

		for (LocalDate localDate = nowInCityZoneId.toLocalDate(); localDate
				.isBefore(localDatePlus3); localDate = localDate.plusDays(1)) {

			LocalDateTime utcStartLocalDateTime = getUTCDateTimeByCity(cityZoneId, localDate, startlocalTime);

			LocalDateTime utcEndLocalDateTime = getUTCDateTimeByCity(cityZoneId, localDate, endlocalTime);

			dayTimeForecasts.addAll(getWeatherForecastBetweenStartDateAndEndDate(weatherForecast, utcStartLocalDateTime,
					utcEndLocalDateTime));

			LocalDateTime utcEndPlus1LocalDateTime = getUTCDateTimeByCity(cityZoneId, localDate.plusDays(1),
					startlocalTime);

			nightTimeForecasts.addAll(getWeatherForecastBetweenStartDateAndEndDate(weatherForecast, utcEndLocalDateTime,
					utcEndPlus1LocalDateTime));

			presureTimeForecasts.addAll(getWeatherForecastBetweenStartDateAndEndDate(weatherForecast,
					utcStartLocalDateTime, utcEndPlus1LocalDateTime));
		}

		return createOutputDTO(dayTimeForecasts, nightTimeForecasts, presureTimeForecasts);
	}

	private List<WeatherForecastList> getWeatherForecastBetweenStartDateAndEndDate(WeatherForecast weatherForecast,
			LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime) {
		return weatherForecast.getList().stream()
				.filter(v -> isWeatherForecastEqualorAfterStartDateTime(startLocalDateTime, v))
				.filter(v -> isWeatherForecastEqualorBeforeEndDateTime(endLocalDateTime, v))
				.collect(Collectors.toList());
	}

	private AverageWeatherForecastDTO createOutputDTO(List<WeatherForecastList> dayTimeForecasts,
			List<WeatherForecastList> nightTimeForecasts, List<WeatherForecastList> presureTimeForecasts) {

		double dayAvg = dayTimeForecasts.stream().mapToDouble(val -> val.getMain().getTemp() - KELVIN_CONSTANT)
				.average().orElse(Double.NaN);

		double nightAvg = nightTimeForecasts.stream().mapToDouble(val -> val.getMain().getTemp() - KELVIN_CONSTANT)
				.average().orElse(Double.NaN);

		double pressureAvg = presureTimeForecasts.stream().mapToDouble(val -> val.getMain().getPressure()).average()
				.orElse(Double.NaN);

		return new AverageWeatherForecastDTO(Math.round(dayAvg*100.0)/100.0, Math.round(nightAvg*100.0)/100.0, Math.round(pressureAvg*100.0)/100.0);
	}

	private boolean isWeatherForecastEqualorBeforeEndDateTime(LocalDateTime e, WeatherForecastList v) {
		return LocalDateTime.parse(v.getDt_txt(), formatter).isEqual(e)
				|| LocalDateTime.parse(v.getDt_txt(), formatter).isBefore(e);
	}

	private boolean isWeatherForecastEqualorAfterStartDateTime(LocalDateTime s, WeatherForecastList v) {
		return LocalDateTime.parse(v.getDt_txt(), formatter).isEqual(s)
				|| LocalDateTime.parse(v.getDt_txt(), formatter).isAfter(s);
	}

	private LocalDateTime getUTCDateTimeByCity(Optional<ZoneId> cityZoneId, LocalDate localDate, LocalTime localTime) {
		return ZonedDateTime.of(localDate, localTime, cityZoneId.get()).withZoneSameInstant(ZoneId.of("UTC"))
				.toLocalDateTime();
	}
}
