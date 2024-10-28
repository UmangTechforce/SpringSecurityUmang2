package com.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.config.ResponseHandler;
import com.demo.dto.DepartmentDTO;
import com.demo.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/department")
@Slf4j
@RequiredArgsConstructor
public class DepartmentController {

	private final DepartmentService departmentService;

	@GetMapping
	public ResponseEntity<Object> getDepartments(Principal principal) {

		log.info("Inside DepartmentController getDepartments() --> enter");

		List<DepartmentDTO> departmentDTOs = departmentService.getDepartmentsOfAdmin(principal);

		log.info("Inside DepartmentController getDepartments() --> exit");

		return new ResponseHandler.ResponseBuilder().setData(departmentDTOs).setMessage("All Departments")
				.setStatus(HttpStatus.OK).build().create();
	}
}
