package com.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.AttendanceReport;
import com.demo.entity.Employee;



@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceReport , Long> {
	
	
	Boolean existsByEmployeeAndDate(Employee employee, Date date);
	
	List<AttendanceReport> findByDate(Date date);
}
