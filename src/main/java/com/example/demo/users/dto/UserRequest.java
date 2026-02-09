package com.example.demo.users.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {
	private String nickname;
	private String password;
}
