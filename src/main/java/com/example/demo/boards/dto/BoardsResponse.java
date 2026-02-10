package com.example.demo.boards.dto;

import java.time.LocalDateTime;

import com.example.demo.boards.domain.Boards;
import com.example.demo.users.domain.Users;

import lombok.Getter;

@Getter
public class BoardsResponse {
	private Long id;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Users user;
	
	public BoardsResponse() {}
	
	public BoardsResponse(Boards boards, boolean needUser) {
		this.id = boards.getId();
		this.title = boards.getTitle();
		this.content = boards.getContent();
		this.createdAt = boards.getCreatedAt();
		this.updatedAt = boards.getUpdatedAt();
		
		if (needUser) {
			this.user = new Users();
			this.user.setId(boards.getUser().getId());
			this.user.setNickname(boards.getUser().getNickname());
			this.user.setPassword(null);
		} else {
			this.user = null;
		}
	}
	
	public static BoardsResponse withoutUser(Boards boards) {
		return new BoardsResponse(boards, false);
	}
	
	public static BoardsResponse withUser(Boards boards) {
		return new BoardsResponse(boards, true);
	}
}
