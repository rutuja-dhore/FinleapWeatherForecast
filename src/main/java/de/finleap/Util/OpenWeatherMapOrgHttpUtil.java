package de.finleap.Util;

import java.io.IOException;

import javax.validation.constraints.NotEmpty;

import org.springframework.stereotype.Service;

import de.finleap.exception.ErrorCode;
import de.finleap.exception.ValidationException;
import de.finleap.model.WeatherForecast;
import de.finleap.service.OpenWeatherMapOrgService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Service
public class OpenWeatherMapOrgHttpUtil {

	private static final String APPID = "70aa2bca7d67860a1bdc5834f808f36d";

	private static final String baseUrl = "http://api.openweathermap.org/";

	private OpenWeatherMapOrgHttpUtil(){}
	
	public static WeatherForecast getWeatherForcastByCity(@NotEmpty String city) throws IOException {

		OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

		Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
				.client(httpClient.build()).build();

		OpenWeatherMapOrgService openWeatherService = retrofit.create(OpenWeatherMapOrgService.class);

		Call<WeatherForecast> callSync = openWeatherService.getWeatherForecastByCity(APPID, city);

		Response<WeatherForecast> response = callSync.execute();

		if (isValidateResponse(response))
			return response.body();

		return null;
	}

	private static boolean isValidateResponse(Response<WeatherForecast> response) {

		if (response.isSuccessful()) {
			return true;
		}

		int code = response.code();
		if (code == ErrorCode.CITY_NOT_FOUND.getCode()) {
			throw new ValidationException(ErrorCode.CITY_NOT_FOUND);
		}
		if (code == ErrorCode.VALIDATION_INVALID_API_KEY.getCode()) {
			throw new ValidationException(ErrorCode.VALIDATION_INVALID_API_KEY);
		}
		if (code == ErrorCode.NOTHING_TO_GEOCODE.getCode()) {
			throw new ValidationException(ErrorCode.NOTHING_TO_GEOCODE);
		}

		return false;
	}
}
