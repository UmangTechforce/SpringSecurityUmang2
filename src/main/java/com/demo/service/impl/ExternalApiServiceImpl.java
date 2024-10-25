package com.demo.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.external.response.GenericJsonResponseObject;
import com.demo.external.response.WeatherApiResponse;
import com.demo.service.ExternalApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalApiServiceImpl implements ExternalApiService {

	private final RestTemplate restTemplate;
	private final MessageSource messageSource;
	private final GenericJsonResponseObject genericJsonResponseObject;

	private static final String WEATHER_API = "https://api.tomorrow.io/v4/weather/realtime?location=LOCATION";

	@Override
	public WeatherApiResponse getWeatherApiResponse(final String location) throws Exception {

		log.info("In ExternalApiService getWeatherApiResponse() enter");

		HttpHeaders headers = new HttpHeaders();
		headers.set("apikey", "i2YIXkpuBnTG90E8XITrPVIYnH60lqCw"); // Set the API key in the header
		HttpEntity<String> entity = new HttpEntity<>(headers);
		WEATHER_API.replace("LOCATION",location.toLowerCase());
		
		ResponseEntity<String> response = restTemplate.exchange(WEATHER_API, HttpMethod.GET, entity, String.class);
		
		WeatherApiResponse weatherApiResponse = genericJsonResponseObject.fromJson(response.getBody(), WeatherApiResponse.class);
		
		System.out.println(weatherApiResponse.getData().getValues());
		System.out.println("Temperature of "+location+" is "+weatherApiResponse.getData().getValues().getTemperature());
		

		log.info("In ExternalApiService getWeatherApiResponse() exit");
		return null;
	}

}
