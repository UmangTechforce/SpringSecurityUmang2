package com.demo.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeUpdateDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -1633874818203408306L;

	
	@NotNull(message = "Provide id to update employee")
	private Long id;
	
	@NotBlank(message = "Select role for employee")
	private String role;

	
	private Long departmentId;
	
	@NotNull(message = "Provide status for employee")
	private Boolean active;
}
