package com.demo.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.demo.dto.DepartmentDTO;

@Component
public class DepartmentVaidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return DepartmentDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		if(target instanceof DepartmentDTO departmentDTO ) {
			
		}
		
	}

}
