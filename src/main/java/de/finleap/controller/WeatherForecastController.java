package de.finleap.controller;

import java.io.IOException;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.finleap.dto.AverageWeatherForecastDTO;
import de.finleap.service.WeatherForecastService;

@RestController
@RequestMapping("/data")
public class WeatherForecastController {

	@Autowired
	private WeatherForecastService weatherForecastService;

	@GetMapping(produces = "application/json")
	public AverageWeatherForecastDTO getWeatherForecastByCity(@RequestParam(required = true) @NotEmpty String city)
			throws IOException {
		return weatherForecastService.getAverageWeatherForcastByCity(city);
	}

}
