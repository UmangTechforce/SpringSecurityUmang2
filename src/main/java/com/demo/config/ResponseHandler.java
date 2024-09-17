package com.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;


@Getter
public class ResponseHandler {

	private String message;
	private Object data;
	private Boolean hasNext;
	private Boolean hasPrevious;
	private Integer totalCount;
	private Integer pageNumber;
	private HttpStatus status;

	private ResponseHandler(ResponseBuilder responseBuilder) {
		this.message = responseBuilder.message;
		this.data = responseBuilder.data;
		this.hasNext = responseBuilder.hasNext;
		this.hasPrevious = responseBuilder.hasPrevious;
		this.totalCount = responseBuilder.totalCount;
		this.pageNumber = responseBuilder.pageNumber;
		this.status = responseBuilder.status;

	}

	public static class ResponseBuilder {

		private String message;
		private Object data;
		private Boolean hasNext;
		private Boolean hasPrevious;
		private Integer totalCount;
		private Integer pageNumber;
		private HttpStatus status;

		public ResponseBuilder setMessage(String message) {
			this.message = message;
			return this;
		}

		public ResponseBuilder setData(Object data) {
			this.data = data;
			return this;
		}

		public ResponseBuilder setHasNext(Boolean hasNext) {
			this.hasNext = hasNext;
			return this;
		}

		public ResponseBuilder setHasPrevious(Boolean hasPrevious) {
			this.hasPrevious = hasPrevious;
			return this;
		}

		public ResponseBuilder setTotalCount(Integer totalCount) {
			this.totalCount = totalCount;
			return this;
		}

		public ResponseBuilder setPageNumber(Integer pageNumber) {
			this.pageNumber = pageNumber;
			return this;
		}

		public ResponseBuilder setStatus(HttpStatus status) {
			this.status = status;
			return this;
		}

		public ResponseHandler build() {

			return new ResponseHandler(this);
		}

	}

	private Map<String, Object> responseMap = new HashMap<>();
	
	public ResponseEntity<Object> create() {
		
		responseMap.put("status", status);
		
		if (message != null) {
			responseMap.put("message", message);
		}
		if (data != null) {
			responseMap.put("data", data);
		}
		if (hasNext != null) {
			responseMap.put("hasNext", hasNext);
		}
		if (hasPrevious != null) {
			responseMap.put("hasPrevious", hasPrevious);
		}
		if(pageNumber!=null) {
			responseMap.put("pageNumber", pageNumber);
		}
		if(totalCount!=null) {
			responseMap.put("totalCount", totalCount);
		}
	
		return new ResponseEntity<>(responseMap, status);

	}

}
