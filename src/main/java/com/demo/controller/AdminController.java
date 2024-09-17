package com.demo.controller;

import java.security.Principal;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.config.ResponseHandler;
import com.demo.dto.DepartmentDTO;
import com.demo.dto.EmployeeRequstDTO;
import com.demo.dto.EmployeeResponseDTO;
import com.demo.dto.EmployeeUpdateDTO;
import com.demo.service.DepartmentService;
import com.demo.service.EmployeeService;
import com.demo.validator.EmployeeValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

	private final MessageSource messageSource;
	private final EmployeeService employeeService;
	private final DepartmentService departmentService;
	private final EmployeeValidator employeeValidator;

	
	
	@InitBinder
	public void initbinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(employeeValidator);
	}
	
	
	/**
	 * 
	 * Handler to add department
	 * 
	 * 
	 * @param departmentDTO
	 * @param principal
	 * @return
	 */
	@PostMapping("/add/department")
	public ResponseEntity<Object> addDepartment(@Valid @RequestBody DepartmentDTO departmentDTO,
			final Principal principal) {

		log.info("In AdminController inside addDepartment() --Enter");

		departmentDTO = departmentService.addDepartment(departmentDTO, principal);

		log.info("In AdminController inside addDepartment() --Exit");

		return new ResponseHandler.ResponseBuilder().setData(departmentDTO)
				.setMessage(messageSource.getMessage("department.created", null, null)).setStatus(HttpStatus.CREATED)
				.build().create();
	}

	/**
	 * 
	 * Handler for admin to add Employee
	 * 
	 * @param employeeRequstDTO
	 * @return
	 */
	@PostMapping("/add/employee")
	public ResponseEntity<Object> addEmployee(@Valid @RequestBody final EmployeeRequstDTO employeeRequstDTO,
			final Principal principal) {

		log.info("In AdminController inside addEmployee() --Enter");

		EmployeeResponseDTO employeeResponseDTO = employeeService.addEmployee(employeeRequstDTO, principal);

		log.info("In AdminController inside addEmployee() --Exit");

		return new ResponseHandler.ResponseBuilder().setData(employeeResponseDTO)
				.setMessage(messageSource.getMessage("employee.add", null, null)).setStatus(HttpStatus.CREATED).build()
				.create();

	}

	@PutMapping("/employee")
	public ResponseEntity<Object> updateEmployee(@Valid @RequestBody final EmployeeUpdateDTO employeeUpdateDTO) {
		
		log.info("In AdminController inside updateEmployee() --Enter");
		
		EmployeeResponseDTO employeeResponseDTO = employeeService.updateEmployeByAdmin(employeeUpdateDTO);

		log.info("In AdminController inside updateEmployee() --Exit");
		
		return new ResponseHandler.ResponseBuilder()
				.setData(employeeResponseDTO)
				.setMessage(messageSource.getMessage("employee.update", null, null)).setStatus(HttpStatus.ACCEPTED)
				.build()
				.create();

	}

}
