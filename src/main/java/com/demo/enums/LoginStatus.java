package com.demo.enums;

public enum LoginStatus {
		
	LOG_IN("LOG-IN") , LOG_OUT("LOG-OUT");
	
	private String status;

	private LoginStatus(String status) {

		this.status = status;
	}

	
}
