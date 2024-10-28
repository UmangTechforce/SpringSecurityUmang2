package com.demo.service;

import java.security.Principal;
import java.util.List;

import com.demo.dto.DepartmentDTO;
import com.demo.entity.Department;
import com.demo.entity.Office;

public interface DepartmentService {
	
	/**
	 * 
	 * Service to add Department
	 * 
	 * 
	 * @param departmentDTO
	 * @return
	 */
	DepartmentDTO addDepartment(DepartmentDTO departmentDTO, Principal principal);
	
	/**
	 * 
	 * Service to get department by id
	 * 
	 * @param id
	 * @return
	 */
	Department getDepartmentById(Long id);
	
	
	/**
	 * 
	 * Service to check if department is present by name in the office
	 * 
	 * 
	 * @param name
	 * @param office
	 * @return
	 */
	Boolean isDepartmentPresent(String name, Office office);
	
	
	/***
	 * 
	 * Service to get department of office by name
	 * 
	 * 
	 * @param name
	 * @param office
	 * @return
	 */
	Department getDepartmentByNameAndOffice(String name ,Office office );
	
	
	/**
	 * Service to get all department of the admin's office
	 * 
	 * @param principal
	 * @return
	 */
	List<DepartmentDTO> getDepartmentsOfAdmin(Principal principal);
	
}
