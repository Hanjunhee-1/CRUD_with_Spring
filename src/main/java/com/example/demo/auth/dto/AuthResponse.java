package com.example.demo.auth.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "로그인 응답 (JWT 토큰 발급)")
@Getter
public class AuthResponse {
	
	@Schema(description = "JWT 토큰")
	private String token;
	
	@Schema(description = "토큰 유효 기간")
	private Date expiration;
	
	public AuthResponse() {}
	
	public AuthResponse(String token, Date expiration) {
		this.token = token;
		this.expiration = expiration;
	}
}
