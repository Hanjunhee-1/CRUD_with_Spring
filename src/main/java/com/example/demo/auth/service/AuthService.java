package com.example.demo.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.auth.JwtUtil;
import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.exception.ApiException;
import com.example.demo.exception.ExceptionCode;
import com.example.demo.users.domain.Users;
import com.example.demo.users.dto.UserRequest;
import com.example.demo.users.repository.UsersRepository;

@Service
public class AuthService {
	private final UsersRepository usersRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	
	public AuthService(
			UsersRepository usersRepository, 
			PasswordEncoder passwordEncoder, 
			JwtUtil jwtUtil
		) {
		this.usersRepository = usersRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}
	
	public AuthResponse logIn(UserRequest userRequest) {
		if (userRequest.getNickname() == null || userRequest.getNickname().isEmpty()) {
			throw new ApiException(ExceptionCode.INVALID_NICKNAME);
		}
		Users foundUser = usersRepository.findByNickname(userRequest.getNickname()).orElse(null);
		
		if (foundUser == null) {
			throw new ApiException(ExceptionCode.NOT_FOUND_NICKNAME);
		}
		
		if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
			throw new ApiException(ExceptionCode.INVALID_PASSWORD);
		}
		
		if (!passwordEncoder.matches(userRequest.getPassword(), foundUser.getPassword())) {
			throw new ApiException(ExceptionCode.NOT_FOUND_PASSWORD);
		}
		
		String jwtToken = jwtUtil.createAccessToken(foundUser);
		return new AuthResponse(
					jwtToken,
					jwtUtil.extractExpiration(jwtToken)
				);
	}
}
