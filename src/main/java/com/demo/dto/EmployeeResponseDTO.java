package com.demo.dto;

import java.io.Serializable;

import com.demo.entity.Office;

import lombok.Data;

@Data
public class EmployeeResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7825162445279795786L;
	private Long id;
	private String name;
	private String role;
	private String password;
	private Office office;
	private String email;
	private DepartmentDTO departmentDTO;

}
