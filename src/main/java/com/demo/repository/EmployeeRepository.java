package com.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmail(String email);
	
	Optional<Employee> findByEmailAndActive(String email , Boolean active);
	
	Optional<Employee> findById(Long id);
	
	Boolean existsByEmailIgnoreCase(String email);
	
	Boolean existsByEmailIgnoreCaseAndIdIsNot(String email , Long id);
	
	

}
