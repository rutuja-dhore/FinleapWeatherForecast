package de.finleap.model;

import java.util.List;

import lombok.Data;

@Data
public class WeatherForecast {

	private List<WeatherForecastList> list;
	private City city;

}
