package com.example.demo.users.dto;

import com.example.demo.users.domain.Users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "사용자 단일 응답")
@Getter
public class UsersResponse {
	
	@Schema(description = "사용자 고유 아이디", example = "1")
	private Long id;
	
	@Schema(description = "사용자 닉네임", example = "test2")
	private String nickname;
	
	@Schema(description = "사용자 비밀번호", example = "null")
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
