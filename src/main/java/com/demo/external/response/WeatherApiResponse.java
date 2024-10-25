package com.demo.external.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
public class WeatherApiResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4805977561598602977L;

	private Data data;
	private Location location;
	
	
	
	@Getter
	@Setter
	@ToString
	public static class Data {
		private String time;
		private Values values;

		// Getters and Setters
		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public Values getValues() {
			return values;
		}

		public void setValues(Values values) {
			this.values = values;
		}
	}

	@Getter
	@Setter
	@ToString
	public static class Values {
		private double cloudBase;
		private double cloudCeiling;
		private int cloudCover;
		private double dewPoint;
		private double freezingRainIntensity;
		private int humidity;
		private int precipitationProbability;
		private double pressureSurfaceLevel;
		private double rainIntensity;
		private double sleetIntensity;
		private double snowIntensity;
		private double temperature;
		private double temperatureApparent;
		private int uvHealthConcern;
		private int uvIndex;
		private double visibility;
		private int weatherCode;
		private double windDirection;
		private double windGust;
		private double windSpeed;

	}

	@Getter
	@Setter
	@ToString
	public static class Location {
		private double lat;
		private double lon;
		private String name;
		private String type;

	}

}
