package com.demo.service;

import java.security.Principal;

import com.demo.dto.DepartmentDTO;
import com.demo.entity.Department;

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

}
