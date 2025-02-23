package com.blogApplication.app.payloads;

import lombok.Data;

@Data
public class JwtAuthRequest {

	private String userName;
	
	private String password;
}
