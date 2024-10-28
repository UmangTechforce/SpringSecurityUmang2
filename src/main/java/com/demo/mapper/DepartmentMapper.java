package com.demo.mapper;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.demo.dto.DepartmentDTO;
import com.demo.entity.Department;

@Component
public class DepartmentMapper {
	/**
	 * 
	 * Mapper to convert DepartmentDTO to Department
	 * 
	 * @param departmentDTO
	 * @return
	 */
	public Department toDepartment(DepartmentDTO departmentDTO) {

		Department department = new Department();
		BeanUtils.copyProperties(departmentDTO, department);
		department.setActive(Boolean.TRUE);
		return department;

	}
	
	/**
	 * 
	 * 
	 * Mapper to convert Department to DepartmentDTO
	 * 
	 * @param department
	 * @return
	 */
	public DepartmentDTO toDepartmentDTO(Department department) {
		DepartmentDTO departmentDTO = new DepartmentDTO();
		BeanUtils.copyProperties(department, departmentDTO);
		return departmentDTO;
	}
	
	
	
	
	public List<DepartmentDTO> toDepartmentDTOs(List<Department> departments){
		return departments.stream().map(this::toDepartmentDTO).toList();
	}
	
	
}
