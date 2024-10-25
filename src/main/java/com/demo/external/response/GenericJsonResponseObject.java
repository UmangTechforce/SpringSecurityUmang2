package com.demo.external.response;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Component
public class GenericJsonResponseObject {

	private final ObjectMapper objectMapper;

	public <T> T fromJson(String json, Class<T> valueType) throws Exception {
		return objectMapper.readValue(json, valueType);
	}

}
