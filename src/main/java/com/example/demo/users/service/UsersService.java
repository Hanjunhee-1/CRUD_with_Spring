package com.example.demo.users.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.users.domain.Users;
import com.example.demo.users.dto.UserRequest;
import com.example.demo.users.dto.UsersResponse;
import com.example.demo.users.repository.UsersRepository;

@Service
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
	
	public UsersResponse getMe(Long id) {
		Users user = usersRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		return UsersResponse.withoutPassword(user);
	}
	
	public UsersResponse createUser(UserRequest createUserRequest) {
		if (usersRepository.existsByNickname(createUserRequest.getNickname())) {
			throw new RuntimeException("Nickname already exists");
		}
		
		Users user = new Users();
		user.setNickname(createUserRequest.getNickname());
		user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
		
		Users savedUser = usersRepository.save(user);
		
		return UsersResponse.withPassword(savedUser);
	}
	
	public List<UsersResponse> getAllUser() {
		return usersRepository.findAll().stream()
				.map(user -> UsersResponse.withoutPassword(user))
				.toList();
	}
	
	public UsersResponse updateMe(Long id, UserRequest updateUserRequest) {
		Optional<Users> foundUser = usersRepository.findById(id);
		
		if (foundUser == null) {
			throw new RuntimeException("User not found");
		}
		
		if (!updateUserRequest.getNickname().isEmpty() && usersRepository.existsByNickname(updateUserRequest.getNickname())) {
			throw new RuntimeException("Nickname already exists");
		}
		
		foundUser.get().setNickname(
				updateUserRequest.getNickname() == null || updateUserRequest.getNickname().isEmpty() 
						? foundUser.get().getNickname()
						: updateUserRequest.getNickname());
		foundUser.get().setPassword(
				updateUserRequest.getPassword() == null || updateUserRequest.getPassword().isEmpty()
						? foundUser.get().getPassword()
						: passwordEncoder.encode(updateUserRequest.getPassword())); 
		return UsersResponse.withPassword(usersRepository.save(foundUser.get()));
	}
	
	public void deleteUser(Long id) {
		if (!usersRepository.existsById(id)) {
			throw new RuntimeException("User not found");
		}
		
		usersRepository.deleteById(id);
	}
}
