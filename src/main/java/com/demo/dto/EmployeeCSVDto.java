package com.demo.dto;

import java.io.Serializable;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class EmployeeCSVDto implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -926730871233094983L;
	
	@CsvBindByName( column = "name")
	private String name;

	@CsvBindByName( column = "role")
	private String role;

	@CsvBindByName( column = "password")
	private String password;

	@CsvBindByName( column = "email")
	private String email;

	@CsvBindByName( column = "department")
	private String department;

}
