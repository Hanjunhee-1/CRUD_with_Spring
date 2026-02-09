package com.example.demo.users.dto;

import com.example.demo.users.domain.Users;

import lombok.Getter;

@Getter
public class UsersResponse {
	private Long id;
	private String nickname;
	private String password;
	
	public UsersResponse() {}
	
	public UsersResponse(Users users, boolean needPassword) {
		this.id = users.getId();
		this.nickname = users.getNickname();
		this.password = needPassword ? users.getPassword() : null;
	}
	
	public static UsersResponse withoutPassword(Users users) {
		return new UsersResponse(users, false);
	}
	
	public static UsersResponse withPassword(Users users) {
		return new UsersResponse(users, true);
	}
}
