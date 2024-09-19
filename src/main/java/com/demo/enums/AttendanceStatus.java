package com.demo.enums;

public enum AttendanceStatus {

	
	PRESENT("PRESENT") , ABSENT("ABSENT");
	
	private String status;

	private AttendanceStatus(String status) {

		this.status = status;
	}
	
}
