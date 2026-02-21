package com.example.demo.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.service.AuthService;
import com.example.demo.users.dto.UserRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "로그인/아웃 관련 API")
@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@Operation(summary = "로그인", description = "사용자 정보(nickname, password) 를 입력받아 JWT 를 발급해줍니다.")
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> logIn(@RequestBody UserRequest userRequest) {
		AuthResponse authResponse = this.authService.logIn(userRequest);
		return ResponseEntity.ok(authResponse);
	}
}
