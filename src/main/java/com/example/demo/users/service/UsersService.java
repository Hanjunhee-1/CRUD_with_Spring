package com.example.demo.users.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.ApiException;
import com.example.demo.exception.ExceptionCode;
import com.example.demo.users.domain.Users;
import com.example.demo.users.dto.UserRequest;
import com.example.demo.users.dto.UsersResponse;
import com.example.demo.users.repository.UsersRepository;

@Service
@Transactional
public class UsersService {
	
	private final UsersRepository usersRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UsersService(
			UsersRepository usersRepository,
			PasswordEncoder passwordEncoder
		) {
		this.usersRepository = usersRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional(readOnly = true)
	public UsersResponse getMe(Long id) {
		Users user = usersRepository.findById(id)
				.orElse(null);
		if (user == null) {
			throw new ApiException(ExceptionCode.NOT_FOUND_USER);
		}
		
		return UsersResponse.withoutPassword(user);
	}
	
	public UsersResponse createUser(UserRequest createUserRequest) {
		if (usersRepository.existsByNickname(createUserRequest.getNickname())) {
			throw new ApiException(ExceptionCode.CONFLICT_NICKNAME);
		}
		
		Users user = new Users();
		user.setNickname(createUserRequest.getNickname());
		user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
		
		Users savedUser = usersRepository.save(user);
		
		return UsersResponse.withPassword(savedUser);
	}
	
	@Transactional(readOnly = true)
	public List<UsersResponse> getAllUser() {
		return usersRepository.findAll().stream()
				.map(user -> UsersResponse.withoutPassword(user))
				.toList();
	}
	
	public UsersResponse updateMe(Long id, UserRequest updateUserRequest) {
		Users foundUser = usersRepository.findById(id).orElse(null);
		if (foundUser == null) {
			throw new ApiException(ExceptionCode.NOT_FOUND_USER);
		}
		
		if (updateUserRequest.getNickname() == null || updateUserRequest.getNickname().isEmpty()) {
			throw new ApiException(ExceptionCode.INVALID_NICKNAME);
		}
		
		if (usersRepository.existsByNickname(updateUserRequest.getNickname())) {
			throw new ApiException(ExceptionCode.CONFLICT_NICKNAME);
		}
		
		foundUser.setNickname(
				updateUserRequest.getNickname() == null || updateUserRequest.getNickname().isEmpty() 
						? foundUser.getNickname()
						: updateUserRequest.getNickname());
		foundUser.setPassword(
				updateUserRequest.getPassword() == null || updateUserRequest.getPassword().isEmpty()
						? foundUser.getPassword()
						: passwordEncoder.encode(updateUserRequest.getPassword())); 
		return UsersResponse.withPassword(usersRepository.save(foundUser));
	}
	
	public void deleteUser(Long id) {
		if (!usersRepository.existsById(id)) {
			throw new ApiException(ExceptionCode.NOT_FOUND_USER);
		}
		
		usersRepository.deleteById(id);
	}
}
