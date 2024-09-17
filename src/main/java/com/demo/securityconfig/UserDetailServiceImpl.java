package com.demo.securityconfig;

import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.entity.Employee;
import com.demo.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

	private final EmployeeRepository employeeRepository;
	private final MessageSource messageSource;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Employee employee = employeeRepository.findByEmailAndActive(username, Boolean.TRUE).orElseThrow(() -> new RuntimeException(
				messageSource.getMessage("employee.not.found", new Object[] { username }, null)));

		log.info("In UserDetailServiceImpl loadUserByUsername() method with email " + username);

		return User.builder().username(employee.getEmail())
				.password(employee.getPassword())
				.roles(employee.getRole().toString())
				.build();
	}

}
