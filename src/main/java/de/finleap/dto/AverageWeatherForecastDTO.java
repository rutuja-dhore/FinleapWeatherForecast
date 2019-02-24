package de.finleap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AverageWeatherForecastDTO {

	private double dayAveragefollowing3days;

	private double nightAveragefollowing3days;

	private double pressureAveragefollowing3days;

}
