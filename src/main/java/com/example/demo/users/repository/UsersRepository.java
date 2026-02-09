package com.example.demo.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.users.domain.Users;

public interface UsersRepository extends JpaRepository<Users, Long>{
	boolean existsByNickname(String nickname);
}
