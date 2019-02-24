package de.finleap.controller;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import de.finleap.FinleapApplication;
import de.finleap.dto.AverageWeatherForecastDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { FinleapApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WeatherForecastControllerIntegrationTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void test1_contextLoads() {
	}

	@Test
	public void test3_getAverageWeatherForecastforBerlin_shouldReturnOkStatus() {

		ResponseEntity<AverageWeatherForecastDTO> response = testRestTemplate
				.getForEntity("http://localhost:8080/data?city=berlin", AverageWeatherForecastDTO.class);

		// then
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}