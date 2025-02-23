package com.blogApplication.app.exceptions;

public class ApiExceptions extends RuntimeException {

	public ApiExceptions(String messages) {
		super(messages);
		
	}
	

	public ApiExceptions() {
		super();
		
	}

}
