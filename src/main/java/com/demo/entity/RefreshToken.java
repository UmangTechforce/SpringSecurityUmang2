package com.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class RefreshToken extends CommonModel {
	/**
	* 
	*/
	private static final long serialVersionUID = 1147738057087855167L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String refreshToken;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Employee employee;

}
