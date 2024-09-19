package com.demo.entity;

import java.util.Date;

import com.demo.enums.AttendanceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class AttendanceReport extends CommonModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2517374933289992549L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status" , nullable = false)
	private AttendanceStatus attendanceStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id",nullable =  false)
	private Employee employee;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id")
	private Office office;
	
	

}
