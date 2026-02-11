package com.example.demo.auth.dto;

import java.util.Date;

import lombok.Getter;

@Getter
public class AuthResponse {
	private String token;
	private Date expiration;
	
	public AuthResponse() {}
	
	public AuthResponse(String token, Date expiration) {
		this.token = token;
		this.expiration = expiration;
	}
}
