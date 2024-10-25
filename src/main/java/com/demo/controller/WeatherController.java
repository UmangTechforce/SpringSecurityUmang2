package com.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.service.ExternalApiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weather")
public class WeatherController {

	private final ExternalApiService externalApiService;

	@GetMapping("/{location}")
	public void getWeatherApiResponse(@PathVariable(name = "location")final String location) throws Exception {

		externalApiService.getWeatherApiResponse(location);
	}
}
