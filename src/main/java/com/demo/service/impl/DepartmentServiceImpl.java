package com.demo.service.impl;

import java.security.Principal;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.dto.DepartmentDTO;
import com.demo.entity.Department;
import com.demo.entity.Employee;
import com.demo.mapper.DepartmentMapper;
import com.demo.repository.DepartmentRepository;
import com.demo.repository.EmployeeRepository;
import com.demo.service.DepartmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentMapper departmentMapper;

	private final EmployeeRepository employeeRepository;

	private final MessageSource messageSource;

	private final DepartmentRepository departmentRepository;

	@Override
	public DepartmentDTO addDepartment(DepartmentDTO departmentDTO, Principal principal) {
		Employee admin = employeeRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException(
				messageSource.getMessage("employee.not.found", new Object[] { principal.getName() }, null)));

		Department department = departmentMapper.toDepartment(departmentDTO);
		department.setCreatedBy(admin.getId());
		department.setOffice(admin.getOffice());
		departmentRepository.save(department);
		return departmentMapper.toDepartmentDTO(department);
	}

	@Override
	public Department getDepartmentById(Long id) {

		return departmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException(messageSource.getMessage("department.not.found", null, null)));
	}

}
