package com.demo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.config.ResponseHandler;
import com.demo.dto.DepartmentDTO;
import com.demo.dto.EmployeeRequstDTO;
import com.demo.dto.EmployeeResponseDTO;
import com.demo.dto.EmployeeUpdateDTO;
import com.demo.entity.Employee;
import com.demo.mapper.EmployeeMapper;
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
	private final EmployeeMapper employeeMapper;

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
	
	
	/**
	 * 
	 * Handler to update employee  by admin
	 * 
	 * @param employeeUpdateDTO
	 * @return
	 */
	@PutMapping("/employee")
	public ResponseEntity<Object> updateEmployee(@Valid @RequestBody final EmployeeUpdateDTO employeeUpdateDTO) {

		log.info("In AdminController inside updateEmployee() --Enter");

		EmployeeResponseDTO employeeResponseDTO = employeeService.updateEmployeByAdmin(employeeUpdateDTO);

		log.info("In AdminController inside updateEmployee() --Exit");

		return new ResponseHandler.ResponseBuilder().setData(employeeResponseDTO)
				.setMessage(messageSource.getMessage("employee.update", null, null)).setStatus(HttpStatus.ACCEPTED)
				.build().create();

	}
	
	/**
	 * 
	 * Handler to import csv file of employee
	 * 
	 * 
	 * @param file
	 * @param principal
	 * @return
	 */
	@PostMapping("/file/employee")
	public ResponseEntity<Object> uploadEmployeeCsv(@RequestPart("file") MultipartFile file, Principal principal) {

		log.info("In AdminController inside uploadEmployeeCsv() --Enter");

		Integer count = employeeService.saveImportedEmployee(file, principal);

		log.info("In AdminController inside uploadEmployeeCsv() --Exit");

		return new ResponseHandler.ResponseBuilder().setMessage(count + " data added").setStatus(HttpStatus.CREATED)
				.build().create();

	}
	
	/**
	 * 
	 * 
	 * Handler to export csv of employee
	 * 
	 * @param response
	 * @param principal
	 * @throws IOException
	 */
	@GetMapping("/export/employee")
	public ResponseEntity<Object> exportEmployees(Principal principal) throws IOException {


		byte[] employeeData =  employeeService.exportEmployees(principal);
		
		return ResponseEntity.ok()
				
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=employee.csv")
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(employeeData);
				
	}
	
	/**
	 * 
	 * Handler to change status of employee
	 * 
	 * @param id
	 * @param active
	 * @return
	 */
	@PutMapping("/status/{id}")
	public ResponseEntity<Object> employeeChangeStatus(@PathVariable(name = "id") final Long id,
			@RequestParam(name = "active", required = true) final Boolean active) {

		employeeService.changeStatusEmployee(id, active);
		
		return new ResponseHandler.ResponseBuilder()
				.setMessage(messageSource.getMessage("employee.status",null, null))
				.setStatus(HttpStatus.OK)
				.build()
				.create();
	}
	
	/**
	 * 
	 * 
	 * Handler to get list of all employees
	 * 
	 * @param page
	 * @param size
	 * @param principal
	 * @return
	 */
	@GetMapping("/employees")
	public ResponseEntity<Object> getEmployees(@RequestParam(name = "page", required =false , defaultValue = "1")final Integer page,
	@RequestParam(name = "size", required =false , defaultValue = "7")final Integer size , final Principal  principal){
		
		
		log.info("In AdminController inside getEmployees() --Exit");
		
		Page<Employee> employeePage = employeeService.getEmployees(page-1, size, principal);
		List<EmployeeResponseDTO> employeeResponseDTOs = employeeMapper.toEmployeeResponseDTOs(employeePage.getContent());
		
		log.info("In AdminController inside getEmployees() --Exit");
		
		
		
		
		return new ResponseHandler.ResponseBuilder()
				.setData(employeeResponseDTOs)
				.setMessage("Employees Present")
				.setStatus(HttpStatus.OK)
				.setPageNumber(employeePage.getNumber()+1)
				.setTotalCount(employeePage.getTotalPages())
				.setHasNext(employeePage.hasNext())
				.setTotalPages(employeePage.getTotalPages())
				.setHasPrevious(employeePage.hasPrevious())
				.build()
				.create();
			
	}

}
