package com.demo.service.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.dto.EmployeeRequstDTO;
import com.demo.dto.EmployeeResponseDTO;
import com.demo.dto.EmployeeUpdateDTO;
import com.demo.dto.LoginRequestDTO;
import com.demo.dto.LoginResponseDTO;
import com.demo.entity.Department;
import com.demo.entity.Employee;
import com.demo.enums.Role;
import com.demo.mapper.EmployeeMapper;
import com.demo.repository.EmployeeRepository;
import com.demo.securityconfig.JwtService;
import com.demo.service.DepartmentService;
import com.demo.service.EmployeeService;
import com.demo.util.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository employeeRepository;

	private final AuthenticationManager authenticationManager;

	private final JwtService jwtService;

	private final MessageSource messageSource;

	private final DepartmentService departmentService;

	private final EmployeeMapper employeeMapper;

	@Override
	public LoginResponseDTO userLogin(LoginRequestDTO loginRequestDTO)
			throws InvalidKeyException, NoSuchAlgorithmException {

		log.info("In EmployeeService userLogin() enter");

		LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

		if (authentication.isAuthenticated()) {

			log.info("In EmployeeService userLogin() User is aunthenticated");

			Employee employee = employeeRepository.findByEmail(loginRequestDTO.getEmail())
					.orElseThrow(() -> new RuntimeException(messageSource.getMessage("employee.not.found",
							new Object[] { loginRequestDTO.getEmail() }, null)));
			loginResponseDTO.setEmployee(employee);
			loginResponseDTO.setToken(jwtService.generateToken(loginRequestDTO.getEmail()));
		} else {
			loginResponseDTO.setToken("Login failed");
		}

		return loginResponseDTO;
	}

	@Override
	public EmployeeResponseDTO addEmployee(EmployeeRequstDTO employeeRequstDTO, Principal principal) {

		log.info("In EmployeeService inside addEmployee() --Enter");

		Employee admin = employeeRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException(
				messageSource.getMessage("employee.not.found", new Object[] { principal.getName() }, null)));

		Employee employee = employeeMapper.toEmployee(employeeRequstDTO);
		employee.setOffice(admin.getOffice());
		employeeRepository.save(employee);
		employee.setRole(Role.valueOf(employeeRequstDTO.getRole().toUpperCase()));
		employee.setCreatedBy(admin.getId());
		employee.setDepartment(departmentService.getDepartmentById(employeeRequstDTO.getDepartmentId()));
		employeeRepository.save(employee);

		log.info("In EmployeeService inside addEmployee() --Exit");

		return employeeMapper.toeEmployeeResponseDTO(employee);
	}

	@Override
	public Employee getEmployeeById(final Long id) {

		return employeeRepository.findById(id).orElseThrow(
				() -> new RuntimeException(messageSource.getMessage("employee.not.found", new Object[] { id }, null)));
	}

	@Override
	public EmployeeResponseDTO updateEmployee(EmployeeRequstDTO employeeRequstDTO) {

		log.info("In EmployeeService inside updateEmployee() --Enter");

		Employee employee = getEmployeeById(employeeRequstDTO.getId());
		String role = employee.getRole().toString();
		BeanUtils.copyProperties(employeeRequstDTO, employee);
		employee.setRole(Role.valueOf(role));
		employeeRepository.save(employee);

		log.info("In EmployeeService inside updateEmployee() --Exit");

		return employeeMapper.toeEmployeeResponseDTO(employee);
	}

	@Override
	public EmployeeResponseDTO getEmployeeResponseById(Long id, Principal principal) {

		log.info("In EmployeeService inside getEmployeeResponseById() --Enter");

		Employee employee = getEmployeeById(id);

		if (!employee.getEmail().equals(principal.getName())) {

			log.error("In EmployeeService inside getEmployeeResponseById() --Exception");

			throw new RuntimeException("Token mismatched");
		}
		log.info("In EmployeeService inside getEmployeeResponseById() --Exit");
		return employeeMapper.toeEmployeeResponseDTO(employee);
	}

	@Override
	public EmployeeResponseDTO updateEmployeByAdmin(EmployeeUpdateDTO employeeUpdateDTO) {

		Employee employee = getEmployeeById(employeeUpdateDTO.getId());

		if (employeeUpdateDTO.getDepartmentId() != null) {
			Department department = departmentService.getDepartmentById(employeeUpdateDTO.getDepartmentId());
			employee.setDepartment(department);
		}

		employee.setRole(Role.valueOf(employeeUpdateDTO.getRole()));
		employee.setActive(Boolean.valueOf(employeeUpdateDTO.getActive()));
		employeeRepository.save(employee);

		return employeeMapper.toeEmployeeResponseDTO(employee);
	}

	@Override
	public Boolean existByEmail(EmployeeRequstDTO employeeRequstDTO) {
		
		if(Constants.EMPTY_STRING.test(employeeRequstDTO.getEmail())) {
			if(employeeRequstDTO.getId()==null) {
				return employeeRepository.existsByEmailIgnoreCase(employeeRequstDTO.getEmail());
			}
			else {
				return employeeRepository.existsByEmailIgnoreCaseAndIdIsNot(employeeRequstDTO.getEmail(), employeeRequstDTO.getId());
			}
		}
		
		return false;
	}

}
