package com.example.demo.users.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.users.domain.Users;
import com.example.demo.users.dto.UserRequest;
import com.example.demo.users.dto.UsersResponse;
import com.example.demo.users.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users", description = "사용자 관련 API")
@RestController
@RequestMapping("/users")
public class UsersController {
	
	private final UsersService usersService;
	
	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}

	@Operation(summary = "본인 정보 확인", description = "사용자로부터 정보(JWT 토큰) 를 입력받아 본인 정보를 확인합니다")
	@GetMapping("/me")
	public ResponseEntity<UsersResponse> getMe(Authentication authentication) {
		Users user = (Users) authentication.getPrincipal();
		UsersResponse usersResponse = this.usersService.getMe(user.getId());
		return ResponseEntity.ok(usersResponse);
	}
	
	@Operation(summary = "사용자 생성", description = "사용자로부터 정보(nickname, password) 를 입력받아 사용자를 생성합니다")
	@PostMapping()
	public ResponseEntity<UsersResponse> createUser(@RequestBody UserRequest createUserRequest) {
		UsersResponse usersResponse = this.usersService.createUser(createUserRequest);
		return ResponseEntity.ok(usersResponse);
	}
	
	@Operation(summary = "사용자 조회", description = "사용자를 모두 조회합니다")
	@GetMapping()
	public ResponseEntity<List<UsersResponse>> getAllUser() {
		List<UsersResponse> usersResponse = this.usersService.getAllUser();
		return ResponseEntity.ok(usersResponse);
	}
	
	@Operation(summary = "사용자 정보 수정", description = "사용자로부터 정보(JWT 토큰, nickname, password) 를 입력받아 사용자 정보를 수정합니다")
	@PatchMapping()
	public ResponseEntity<UsersResponse> updateMe(
			Authentication authentication, 
			@RequestBody UserRequest updateUserRequest
		) {
		Users user = (Users) authentication.getPrincipal();
		UsersResponse usersResponse = this.usersService.updateMe(user.getId(), updateUserRequest);
		return ResponseEntity.ok(usersResponse);
	}
	
	@Operation(summary = "사용자 정보 삭제", description = "사용자로부터 정보(JWT 토큰) 를 입력받아 사용자 정보를 삭제합니다")
	@DeleteMapping()
	public ResponseEntity<Void> deleteUser(Authentication authentication) {
		Users user = (Users) authentication.getPrincipal();
		usersService.deleteUser(user.getId());
		return ResponseEntity.noContent().build(); // 204
	}
}
