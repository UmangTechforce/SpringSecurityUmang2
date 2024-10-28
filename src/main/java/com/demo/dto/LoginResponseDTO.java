package com.demo.dto;

import java.io.Serializable;

import com.demo.entity.Employee;

import lombok.Data;

@Data
public class LoginResponseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5833027221163178089L;

	private Employee employee;

	private String token;
	
	private String refreshToken;

}
