package com.demo.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRequstDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5158503496669732188L;

	private Long id;

	@NotBlank(message = "employee.name.not.blank")
	private String name;

	@NotBlank(message = "employee.role.not.blank")
	private String role;

	@NotBlank(message = "employee.password.not.blank")
	private String password;

	@NotBlank(message = "employee.email.not.blank")
	private String email;

	@NotNull(message = "Provide department Id")
	private Long departmentId;

}
