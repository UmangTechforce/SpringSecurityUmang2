package com.demo.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DepartmentDTO implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -5015386206749830358L;

	private Long id;

	private String name;
}
