package com.demo.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.demo.dto.DepartmentDTO;
import com.demo.dto.EmployeeRequstDTO;
import com.demo.dto.EmployeeUpdateDTO;
import com.demo.enums.Role;
import com.demo.service.EmployeeService;
import com.demo.util.Constants;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeValidator implements Validator {

	private final EmployeeService employeeService;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return EmployeeRequstDTO.class.equals(clazz) || DepartmentDTO.class.equals(clazz)
				|| EmployeeUpdateDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		if (target instanceof EmployeeRequstDTO employeeRequstDTO) {

			if (Constants.EMPTY_STRING.test(employeeRequstDTO.getRole())
					&& !Role.getName(employeeRequstDTO.getRole())) {

				errors.rejectValue("role", "401", "Invalid Role");

			}
			if (employeeService.existByEmail(employeeRequstDTO)) {
				errors.rejectValue("email", "401", "Email already exist");
			}

		}

	}

}
