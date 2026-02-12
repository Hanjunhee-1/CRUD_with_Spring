package com.example.demo.boards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.boards.domain.Boards;

public interface BoardsRepository 
		extends JpaRepository<Boards, Long>, JpaSpecificationExecutor<Boards>{
	
}
