package com.demo.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.demo.dto.EmployeeCSVDto;
import com.demo.dto.EmployeeRequstDTO;
import com.demo.dto.EmployeeResponseDTO;
import com.demo.dto.EmployeeUpdateDTO;
import com.demo.dto.LoginRequestDTO;
import com.demo.dto.LoginResponseDTO;
import com.demo.entity.Department;
import com.demo.entity.Employee;
import com.demo.entity.Office;
import com.demo.enums.Role;
import com.demo.mapper.EmployeeMapper;
import com.demo.repository.EmployeeRepository;
import com.demo.securityconfig.JwtService;
import com.demo.service.DepartmentService;
import com.demo.service.EmployeeService;
import com.demo.util.Constants;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

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

		if (Constants.EMPTY_STRING.test(employeeRequstDTO.getEmail())) {
			if (employeeRequstDTO.getId() == null) {
				return employeeRepository.existsByEmailIgnoreCase(employeeRequstDTO.getEmail());
			} else {
				return employeeRepository.existsByEmailIgnoreCaseAndIdIsNot(employeeRequstDTO.getEmail(),
						employeeRequstDTO.getId());
			}
		}

		return false;
	}

	@Override
	public Integer saveImportedEmployee(MultipartFile file, Principal principal) {

		log.info("In EmployeeService inside saveImportedEmployee() --Enter");

		
		Integer count = 0;
		List<EmployeeCSVDto> employeesCsv = extractDataFromCsv(file);

		if (employeesCsv.isEmpty() || employeesCsv == null) {
			throw new RuntimeException("Someting went wrong while parsing");

		}
		Employee admin = employeeRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException(
				messageSource.getMessage("employee.not.found", new Object[] { principal.getName() }, null)));
		
		
		log.info("Admin Office is "+admin.getOffice().getName());
		
		
		for (EmployeeCSVDto employeeCSVDto : employeesCsv) {

			if (validateEmployee(employeeCSVDto, admin.getOffice())) {

				Employee employee = employeeMapper.toEmployeeFromCsv(employeeCSVDto);
				employee.setOffice(admin.getOffice());
				employee.setDepartment(departmentService.getDepartmentByNameAndOffice(employeeCSVDto.getDepartment(),
						admin.getOffice()));
				employeeRepository.save(employee);

				count++;

			}
		}

		log.info("In EmployeeService inside saveImportedEmployee() --Exit");
		return count;
	}

	/**
	 * 
	 * Private method for employee Service to extract all records from the csv file
	 * and return list
	 * 
	 * 
	 * @param file
	 * @return
	 */
	private List<EmployeeCSVDto> extractDataFromCsv(MultipartFile file) {

		log.info("In EmployeeService inside PRIVATE extractDataFromCsv() --Enter");

		try {

			Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			HeaderColumnNameMappingStrategy<EmployeeCSVDto> strategy = new HeaderColumnNameMappingStrategy<EmployeeCSVDto>();
			strategy.setType(EmployeeCSVDto.class);

			CsvToBean<EmployeeCSVDto> csvToBean = new CsvToBeanBuilder<EmployeeCSVDto>(reader)

					.withMappingStrategy(strategy).withIgnoreEmptyLine(Boolean.TRUE)
					.withIgnoreLeadingWhiteSpace(Boolean.TRUE).build();

			log.info("In EmployeeService inside PRIVATE extractDataFromCsv() --Exit");

			return csvToBean.parse().stream().toList();

		} catch (Exception e) {
			System.out.println(e);
			log.error("In EmployeeService inside PRIVATE extractDataFromCsv()");

			return null;
		}

	}

	private Boolean validateEmployee(EmployeeCSVDto employeeCSVDto, Office office) {

		log.info("In EmployeeService inside PRIVATE validateEmployee() --Enter");

		if (employeeRepository.existsByEmailIgnoreCaseAndOffice(employeeCSVDto.getEmail(), office)) {
			
			log.error("In EmployeeService inside PRIVATE validateEmployee() Email not valid");
			
			return false;
		}

		if (!departmentService.isDepartmentPresent(employeeCSVDto.getDepartment(), office)) {

			log.error("In EmployeeService inside PRIVATE validateEmployee() Department not valid");
			
			return false;
		}

		if (!Role.getName(employeeCSVDto.getRole()) && Constants.EMPTY_STRING.test(employeeCSVDto.getRole())) {
			return false;
		}

		log.info("In EmployeeService inside PRIVATE validateEmployee() --Exit");
		return true;

	}

}
