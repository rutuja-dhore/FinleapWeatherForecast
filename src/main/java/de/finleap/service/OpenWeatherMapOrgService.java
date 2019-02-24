package de.finleap.service;

import de.finleap.model.WeatherForecast;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapOrgService {

	@GET("data/2.5/forecast")
	Call<WeatherForecast> getWeatherForecastByCity(@Query("APPID") String appId, @Query("q") String city);
}