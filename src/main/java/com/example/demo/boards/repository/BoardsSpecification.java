package com.example.demo.boards.repository;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.boards.domain.Boards;
import com.example.demo.users.domain.Users;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class BoardsSpecification {
	public static Specification<Boards> titleLike(String title) {
		return (root, query, cb) -> cb.like(root.get("title"), "%" + title + "%");
	}
	
	public static Specification<Boards> nicknameLike(String nickname) {
	    return (root, query, cb) -> {
	        Join<Boards, Users> userJoin = root.join("user", JoinType.INNER);
	        return cb.like(userJoin.get("nickname"), "%" + nickname + "%");
	    };
	}
}
