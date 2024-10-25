package com.demo.service;

import com.demo.external.response.WeatherApiResponse;

public interface ExternalApiService {
	
	
	public WeatherApiResponse getWeatherApiResponse(String location) throws Exception;
	
}
