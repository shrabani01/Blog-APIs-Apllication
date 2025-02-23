package com.blogApplication.app.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
	                     AuthenticationException authException) throws IOException, ServletException {
	    System.out.println("Unauthorized request to: " + request.getRequestURI());
	    System.out.println("Error Message: " + authException.getMessage());
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied!!");
	}


}
