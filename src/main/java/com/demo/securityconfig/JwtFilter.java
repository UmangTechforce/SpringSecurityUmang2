package com.demo.securityconfig;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	private final JwtService jwtService;

	@Autowired
	ApplicationContext context;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Calls private method to get a valid JWT tokens
		String token = getToken(request);
		
		log.info("The main token from filter "+token);
		if (StringUtils.hasText(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Logic to get User email and set authentication

			String username = jwtService.getUserName(token);
			UserDetails userDetails = context.getBean(UserDetailServiceImpl.class).loadUserByUsername(username);

			// Getting user deatils from the username in the token
			
			if (jwtService.validateToken(token, userDetails)) {

				// Setting the auth token in the context
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null,userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}

		}

		filterChain.doFilter(request, response);
	}

	// Method to get the token
	public String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		// Checking the Token from header if it hase 'Bearer ' in the begining

		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			
			
			
			return token.substring(7, token.length());

		}
		return null;
	}

}
