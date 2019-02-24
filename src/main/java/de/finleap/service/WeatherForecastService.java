package de.finleap.service;

import java.io.IOException;

import javax.validation.constraints.NotEmpty;

import de.finleap.dto.AverageWeatherForecastDTO;

public interface WeatherForecastService {

	AverageWeatherForecastDTO getAverageWeatherForcastByCity(@NotEmpty String city) throws IOException;

}
