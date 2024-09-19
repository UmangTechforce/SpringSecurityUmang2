package com.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.Employee;
import com.demo.entity.Office;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>  {

	Optional<Employee> findByEmail(String email);

	Optional<Employee> findByEmailAndActive(String email, Boolean active);

	Optional<Employee> findById(Long id);

	Boolean existsByEmailIgnoreCase(String email);

	Boolean existsByEmailIgnoreCaseAndIdIsNot(String email, Long id);

	Boolean existsByEmailIgnoreCaseAndOffice(String email, Office office);

	List<Employee> findByOffice(Office office);
	
	Page<Employee> findByOffice(Office office,Pageable pageable);
	
	List<Employee> findByActive(Boolean active);

}
