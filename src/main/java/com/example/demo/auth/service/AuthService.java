package com.example.demo.auth.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.auth.JwtUtil;
import com.example.demo.auth.dto.AuthResponse;
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
			throw new RuntimeException("Nickname is empty or null");
		}
		Optional<Users> foundUser = usersRepository.findByNickname(userRequest.getNickname());
		
		if (foundUser == null) {
			throw new RuntimeException("Nickname doesn't matched");
		}
		
		if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
			throw new RuntimeException("Password is empty or null");
		}
		
		if (!passwordEncoder.matches(userRequest.getPassword(), foundUser.get().getPassword())) {
			throw new RuntimeException("Password doesn't matched");
		}
		
		String jwtToken = jwtUtil.createAccessToken(foundUser.get());
		return new AuthResponse(
					jwtToken,
					jwtUtil.extractExpiration(jwtToken)
				);
	}
}
