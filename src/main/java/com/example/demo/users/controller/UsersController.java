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

@RestController
@RequestMapping("/users")
public class UsersController {
	
	private final UsersService usersService;
	
	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}

	@GetMapping("/me")
	public ResponseEntity<UsersResponse> getMe(Authentication authentication) {
		Users user = (Users) authentication.getPrincipal();
		UsersResponse usersResponse = this.usersService.getMe(user.getId());
		return ResponseEntity.ok(usersResponse);
	}
	
	@PostMapping()
	public ResponseEntity<UsersResponse> createUser(@RequestBody UserRequest createUserRequest) {
		UsersResponse usersResponse = this.usersService.createUser(createUserRequest);
		return ResponseEntity.ok(usersResponse);
	}
	
	@GetMapping()
	public ResponseEntity<List<UsersResponse>> getAllUser() {
		List<UsersResponse> usersResponse = this.usersService.getAllUser();
		return ResponseEntity.ok(usersResponse);
	}
	
	@PatchMapping()
	public ResponseEntity<UsersResponse> updateMe(
			Authentication authentication, 
			@RequestBody UserRequest updateUserRequest
		) {
		Users user = (Users) authentication.getPrincipal();
		UsersResponse usersResponse = this.usersService.updateMe(user.getId(), updateUserRequest);
		return ResponseEntity.ok(usersResponse);
	}
	
	@DeleteMapping()
	public ResponseEntity<Void> deleteUser(Authentication authentication) {
		Users user = (Users) authentication.getPrincipal();
		usersService.deleteUser(user.getId());
		return ResponseEntity.noContent().build(); // 204
	}
}
