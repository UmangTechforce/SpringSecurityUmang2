package com.demo.service.impl;

import java.security.Principal;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.dto.DepartmentDTO;
import com.demo.entity.Department;
import com.demo.entity.Employee;
import com.demo.entity.Office;
import com.demo.mapper.DepartmentMapper;
import com.demo.repository.DepartmentRepository;
import com.demo.repository.EmployeeRepository;
import com.demo.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentMapper departmentMapper;

	private final EmployeeRepository employeeRepository;

	private final MessageSource messageSource;

	private final DepartmentRepository departmentRepository;

	@Override
	public DepartmentDTO addDepartment(DepartmentDTO departmentDTO, Principal principal) {
		
		log.info("In DepartmentService inside addDepartment() --Enter");
		
		Employee admin = employeeRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException(
				messageSource.getMessage("employee.not.found", new Object[] { principal.getName() }, null)));
		
		
		if(isDepartmentPresent(departmentDTO.getName(), admin.getOffice())) {
			log.error("In DepartmentService inside addDepartment()  --Error");
			
			throw new RuntimeException("Department already exist");
		}
		
		Department department = departmentMapper.toDepartment(departmentDTO);
		department.setCreatedBy(admin.getId());
		department.setOffice(admin.getOffice());
		departmentRepository.save(department);
		
		log.info("In DepartmentService inside addDepartment() --Exit");
		
		return departmentMapper.toDepartmentDTO(department);
	}

	@Override
	public Department getDepartmentById(Long id) {

		return departmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException(messageSource.getMessage("department.not.found", null, null)));
	}

	@Override
	public Boolean isDepartmentPresent(String name, Office office) {

		return departmentRepository.existsByNameIgnoreCaseAndOffice(name, office);
	}

	@Override
	public Department getDepartmentByNameAndOffice(String name, Office office) {

		return departmentRepository.findByNameIgnoreCaseAndOffice(name, office)
				.orElseThrow(() -> new RuntimeException("department.not.found"));
	}

}
