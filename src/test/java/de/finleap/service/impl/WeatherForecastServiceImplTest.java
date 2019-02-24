package de.finleap.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;

import de.finleap.FinleapApplication;
import de.finleap.Util.OpenWeatherMapOrgHttpUtil;
import de.finleap.dto.AverageWeatherForecastDTO;
import de.finleap.exception.CityNotFoundException;
import de.finleap.model.City;
import de.finleap.model.Coordinates;
import de.finleap.model.Main;
import de.finleap.model.WeatherForecast;
import de.finleap.model.WeatherForecastList;
import de.finleap.service.WeatherForecastService;

@RunWith(PowerMockRunner.class)
@SpringBootTest(classes = { FinleapApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PrepareForTest(OpenWeatherMapOrgHttpUtil.class)
public class WeatherForecastServiceImplTest {

	private WeatherForecastService classUnderTest;

	private String cityName = "berlin";

	@Before
	public void setUp() {
		classUnderTest = mock(WeatherForecastService.class);
	}

	@Test
	public void test1_verify_default_average_returned_successfully() throws IOException {
		AverageWeatherForecastDTO avg = getAverageDefaultObject();

		when(classUnderTest.getAverageWeatherForcastByCity(cityName)).thenReturn(avg);

		AverageWeatherForecastDTO result = classUnderTest.getAverageWeatherForcastByCity(cityName);

		assertEquals(result, avg);
	}

	@Test
	public void test2_verify_default_wheatherForecast_response() throws IOException {

		PowerMockito.mockStatic(OpenWeatherMapOrgHttpUtil.class);

		when(OpenWeatherMapOrgHttpUtil.getWeatherForcastByCity(cityName)).thenReturn(getDefaultForecast());

		AverageWeatherForecastDTO avg = getAverageDefaultObject();

		when(classUnderTest.getAverageWeatherForcastByCity(cityName)).thenReturn(avg);

		AverageWeatherForecastDTO result = classUnderTest.getAverageWeatherForcastByCity(cityName);

		assertEquals(result, avg);
	}

	@Test(expected = CityNotFoundException.class)
	public void test3_verify_city_not_found_exception() throws IOException {

		when(classUnderTest.getAverageWeatherForcastByCity("@#")).thenThrow(CityNotFoundException.class);

		classUnderTest.getAverageWeatherForcastByCity("@#");

	}

	private AverageWeatherForecastDTO getAverageDefaultObject() {
		return new AverageWeatherForecastDTO(-1.99, -1.99, 1039.64);
	}

	private WeatherForecast getDefaultForecast() {
		WeatherForecast berlinForecast = new WeatherForecast();

		List<WeatherForecastList> weatherForecasts = new ArrayList<>();

		WeatherForecastList weather1 = new WeatherForecastList();
		weather1.setDt(1550977200);
		weather1.setDt_txt("2019-02-24 03:00:00");
		Main main1 = new Main();
		main1.setPressure(1039.64);
		main1.setTemp(271.16);
		weather1.setMain(main1);

		weatherForecasts.add(weather1);

		Coordinates coord = new Coordinates();
		coord.setLat(52.517);
		coord.setLon(13.3889);

		City city = new City();
		city.setName(cityName);
		city.setCoord(coord);

		berlinForecast.setList(weatherForecasts);
		berlinForecast.setCity(city);

		return berlinForecast;
	}

}
