package com.demo.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserDetailsService userDetailsService;
	private final JwtFilter jwtFilter;

	// Method to set the security configuration
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {

		security.csrf(Customizer -> Customizer.disable());
		security.authorizeHttpRequests(r -> r
				// This expression states any request to admin is only authorized to ADMIN role
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/employee/activity/login").hasAnyRole("ADMIN","EMPLOYEE")
				
				
				//This expression states anylogin or department requests are allowed
				.requestMatchers("/*/login","/weather/**","/login.html").permitAll()
				.anyRequest().authenticated());

		security.sessionManagement(Session -> Session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		security.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return security.build();

	}

	// Method to verify Id and password
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		;
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);

		return daoAuthenticationProvider;

	}

	// To provide AuthenticationManager object to the login service
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {

		return authenticationConfiguration.getAuthenticationManager();
	}

}
