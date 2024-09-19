package com.demo.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.demo.dto.EmployeeRequstDTO;
import com.demo.dto.EmployeeResponseDTO;
import com.demo.dto.EmployeeUpdateDTO;
import com.demo.dto.LoginRequestDTO;
import com.demo.dto.LoginResponseDTO;
import com.demo.entity.Employee;

public interface EmployeeService {

	/**
	 * Service to authenticate the login credentials and generate JWT token
	 * 
	 * 
	 * @param loginRequestDTO
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	LoginResponseDTO userLogin(LoginRequestDTO loginRequestDTO) throws InvalidKeyException, NoSuchAlgorithmException;

	/**
	 * 
	 * Service to add employee by only admin
	 * 
	 * @param employeeResponseDTO
	 * @return
	 */
	EmployeeResponseDTO addEmployee(EmployeeRequstDTO employeeRequstDTO, Principal principal);

	/**
	 * 
	 * Service method to get employee by Id
	 * 
	 * @param id
	 * @return
	 */
	Employee getEmployeeById(Long id);

	/**
	 * 
	 * Service to update Employee
	 * 
	 * @param employeeRequstDTO
	 * @return
	 */
	EmployeeResponseDTO updateEmployee(EmployeeRequstDTO employeeRequstDTO);

	/**
	 * 
	 * 
	 * Service to get a employee response by id
	 * 
	 * @param id
	 * @return
	 */
	EmployeeResponseDTO getEmployeeResponseById(Long id, Principal principal);

	/**
	 * Service to update employee's Role , department , status
	 * 
	 * 
	 * @param employeeUpdateDTO
	 * @return
	 */
	EmployeeResponseDTO updateEmployeByAdmin(EmployeeUpdateDTO employeeUpdateDTO);

	/**
	 * 
	 * Service to validate unique email of employee
	 * 
	 * 
	 * @param employeeRequstDTO
	 * @return
	 */
	Boolean existByEmail(EmployeeRequstDTO employeeRequstDTO);

	/**
	 * 
	 * Service to save the employee with csv file
	 * 
	 * 
	 * @param file
	 * @return
	 */
	Integer saveImportedEmployee(MultipartFile file, Principal principal);

	/**
	 * 
	 * 
	 * Service to export all employees of the admin's office
	 * 
	 */
	byte[] exportEmployees(Principal principal);

	
	/**
	 * Service to change status of employee
	 * 
	 * 
	 * @param id
	 */
	void changeStatusEmployee(Long id, Boolean active);

	/**
	 * 
	 * 
	 * Service to get all employees of admin's office
	 * 
	 * @param principal
	 * @return
	 */
	Page<Employee> getEmployees(Integer page, Integer size , Principal principal);

	
	/**
	 * 
	 * Service to add employee login activity in history
	 * 
	 * @param principal
	 */
	void employeeLoginAttendance(Principal principal);
	
	
	
	/**
	 * 
	 * Service to add employee log-out activity in history
	 * 
	 * @param principal
	 */
	void employeeLogoutAttendance(Principal principal);
}
