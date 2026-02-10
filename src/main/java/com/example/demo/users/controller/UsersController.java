package com.example.demo.users.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.users.dto.UserRequest;
import com.example.demo.users.dto.UsersResponse;
import com.example.demo.users.service.UsersService;

/**
 * 
 * description
 * 
 * what I need to develop?
 * -> CRUD
 * - getMe: It returns your information(id, nickname, password) later it returns jwt token.
 * - createUser: It creates user.
 * - getAllUser: It returns all user's information(id, nickname) later add filtering
 * - updateMe: It updates user's information. It must check user's authority about updating information.
 * - deleteUser: It deletes user's information. It must check user's authority about deleting information.
 * 
 */

@RestController
@RequestMapping("/users")
public class UsersController {
	
	private final UsersService usersService;
	
	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UsersResponse> getMe(@PathVariable("userId") Long id) {
		UsersResponse usersResponse = this.usersService.getMe(id);
		return ResponseEntity.ok(usersResponse);
	}
	
	@PostMapping("")
	public ResponseEntity<UsersResponse> createUser(@RequestBody UserRequest createUserRequest) {
		UsersResponse usersResponse = this.usersService.createUser(createUserRequest);
		return ResponseEntity.ok(usersResponse);
	}
	
	@GetMapping("")
	public ResponseEntity<List<UsersResponse>> getAllUser() {
		List<UsersResponse> usersResponse = this.usersService.getAllUser();
		return ResponseEntity.ok(usersResponse);
	}
	
	@PatchMapping("/{userId}")
	public ResponseEntity<UsersResponse> updateMe(
			@PathVariable("userId") Long id, 
			@RequestBody UserRequest updateUserRequest
		) {
		UsersResponse usersResponse = this.usersService.updateMe(id, updateUserRequest);
		return ResponseEntity.ok(usersResponse);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long id) {
		usersService.deleteUser(id);
		return ResponseEntity.noContent().build(); // 204
	}
}
