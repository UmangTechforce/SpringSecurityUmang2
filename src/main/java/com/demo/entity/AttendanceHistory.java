package com.demo.entity;

import java.util.Date;

import org.hibernate.annotations.CurrentTimestamp;

import com.demo.enums.LoginStatus;

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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "attendance_history")
@Data
@EqualsAndHashCode(callSuper = false)
public class AttendanceHistory extends CommonModel {/**
	 * 
	 */
	private static final long serialVersionUID = 4717574751735969663L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "activity", nullable = false)
	private LoginStatus loginStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.TIME)
	@CurrentTimestamp
	private Date time;
	

}
