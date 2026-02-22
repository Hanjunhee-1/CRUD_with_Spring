package com.example.demo.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "사용자 요청")
@Setter
@Getter
public class UserRequest {
	
	@Schema(description = "사용자 닉네임", example = "test2")
	private String nickname;
	
	@Schema(description = "사용자 비밀번호", example = "1234")
	private String password;
}
