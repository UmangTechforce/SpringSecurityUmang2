package com.demo.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.config.ResponseHandler;
import com.demo.dto.EmployeeRequstDTO;
import com.demo.dto.EmployeeResponseDTO;
import com.demo.dto.LoginRequestDTO;
import com.demo.dto.LoginResponseDTO;
import com.demo.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/employee")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;

	private final MessageSource messageSource;

	/**
	 * Handler to accept login credentials login to system
	 * 
	 * 
	 * @param loginRequestDTO
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody final LoginRequestDTO loginRequestDTO)
			throws InvalidKeyException, NoSuchAlgorithmException {

		log.info("In EmployeeController inside login() --Enter");

		LoginResponseDTO loginResponseDTO = employeeService.userLogin(loginRequestDTO);

		log.info("In EmployeeController inside login() --Exit");

		return new ResponseHandler.ResponseBuilder().setData(loginResponseDTO).setStatus(HttpStatus.OK)
				.setMessage(messageSource.getMessage("employee.login", null, null)).setJwtToken(loginResponseDTO.getToken()).build().create();
	}

	/**
	 * 
	 * Handler for Employee to update details
	 * 
	 * @param employeeRequstDTO
	 * @return
	 */
	@PutMapping
	public ResponseEntity<Object> updateEmployee(@Valid @RequestBody final EmployeeRequstDTO employeeRequstDTO) {

		log.info("In EmployeeController inside updateEmployee() --Enter");

		EmployeeResponseDTO employeeResponseDTO = employeeService.updateEmployee(employeeRequstDTO);

		log.info("In EmployeeController inside updateEmployee() --Exit");

		return new ResponseHandler.ResponseBuilder().setData(employeeResponseDTO)
				.setMessage(messageSource.getMessage("employee.update", null, null)).setStatus(HttpStatus.ACCEPTED)
				.build().create();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getEmployee(@PathVariable final Long id, Principal principal) {

		log.info("In EmployeeController inside getEmployee() --Enter");

		EmployeeResponseDTO employeeResponseDTO = employeeService.getEmployeeResponseById(id, principal);

		log.info("In EmployeeController inside getEmployee() --Exit");

		return new ResponseHandler.ResponseBuilder().setData(employeeResponseDTO)
				.setMessage(messageSource.getMessage("employee.present", null, null)).setStatus(HttpStatus.OK)
				.build().create();
	}

	@PostMapping("/activity/login")
	public ResponseEntity<Object> employeeLoginHistory(final Principal principal) {

		log.info("In EmployeeController inside employeeLoginHistory() --Enter");

		employeeService.employeeLoginAttendance(principal);

		log.info("In EmployeeController inside employeeLoginHistory() --Exit");

		return new ResponseHandler.ResponseBuilder()
				.setMessage(messageSource.getMessage("employee.activity.login", null, null))
				.setStatus(HttpStatus.CREATED).build().create();
	}

	@PostMapping("/activity/logout")
	public ResponseEntity<Object> employeeLogoutHistory(final Principal principal) {

		log.info("In EmployeeController inside employeeLogoutHistory() --Enter");

		employeeService.employeeLogoutAttendance(principal);

		log.info("In EmployeeController inside employeeLogoutHistory() --Exit");

		return new ResponseHandler.ResponseBuilder()
				.setMessage(messageSource.getMessage("employee.activity.logout", null, null))
				.setStatus(HttpStatus.CREATED)
				.build()
				.create();

	}

}
