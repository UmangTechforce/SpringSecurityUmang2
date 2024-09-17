package com.demo.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.demo.dto.EmployeeRequstDTO;
import com.demo.dto.EmployeeResponseDTO;
import com.demo.entity.Employee;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

	private final DepartmentMapper departmentMapper;

	/**
	 * 
	 * Mapper method to convert EmployeeRequstDTO to Employee
	 * 
	 * @param employeeRequstDTO
	 * @return
	 */
	public Employee toEmployee(EmployeeRequstDTO employeeRequstDTO) {
		Employee employee = new Employee();
		BeanUtils.copyProperties(employeeRequstDTO, employee);
		employee.setPassword(passwordEncoder.encode(employeeRequstDTO.getPassword()));
		employee.setActive(Boolean.TRUE);
		return employee;
	}

	/**
	 * 
	 * Mapper method to convert Employee to EmployeeResponseDTO
	 * 
	 * @param employee
	 * @return
	 */
	public EmployeeResponseDTO toeEmployeeResponseDTO(Employee employee)

	{
		EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO();
		BeanUtils.copyProperties(employee, employeeResponseDTO);
		employeeResponseDTO.setRole(employee.getRole().toString());
		employeeResponseDTO.setDepartmentDTO(departmentMapper.toDepartmentDTO(employee.getDepartment()));
		return employeeResponseDTO;
	}
}
