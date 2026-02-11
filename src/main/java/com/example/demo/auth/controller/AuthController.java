package com.example.demo.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.service.AuthService;
import com.example.demo.users.dto.UserRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping
	public ResponseEntity<AuthResponse> logIn(@RequestBody UserRequest userRequest) {
		AuthResponse authResponse = this.authService.logIn(userRequest);
		return ResponseEntity.ok(authResponse);
	}
	
	// logOut 구현
}
