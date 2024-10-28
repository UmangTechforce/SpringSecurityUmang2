package com.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.Department;
import com.demo.entity.Office;



@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
		
	Boolean existsByNameIgnoreCaseAndOffice(String name, Office office);
	
	Optional<Department>  findByNameIgnoreCaseAndOffice(String name, Office office);
	
	List<Department> findByOffice(Office office);
}
