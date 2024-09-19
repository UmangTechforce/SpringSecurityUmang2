package com.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.demo.dto.EmployeeCSVDto;
import com.demo.dto.EmployeeRequstDTO;
import com.demo.dto.EmployeeResponseDTO;
import com.demo.entity.Employee;
import com.demo.enums.Role;

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
		if(employee.getDepartment()!=null) {
			employeeResponseDTO.setDepartmentDTO(departmentMapper.toDepartmentDTO(employee.getDepartment()));
		}
		return employeeResponseDTO;
			
	}
	
	/**
	 * 
	 * Mapper method to convert List of Employee to List of Employee Response Dto
	 * 
	 * @param employees
	 * @return
	 */
	public List<EmployeeResponseDTO> toEmployeeResponseDTOs(List<Employee> employees){
		
		return employees.stream().map(this::toeEmployeeResponseDTO).collect(Collectors.toList());
	}
	
	
	/**
	 * 
	 * Mapper method to convert EmployeeCSVDto to Employee
	 * 
	 * 
	 * @param employeeCSVDto
	 * @return
	 */
	public Employee toEmployeeFromCsv(EmployeeCSVDto employeeCSVDto) {
		
		Employee employee = new Employee();
		BeanUtils.copyProperties(employeeCSVDto, employee);
		employee.setRole(Role.valueOf(employeeCSVDto.getRole().toUpperCase()));
		employee.setActive(Boolean.TRUE);
		employee.setPassword(passwordEncoder.encode(employeeCSVDto.getPassword()));
	
		
		return employee;
	}
}
