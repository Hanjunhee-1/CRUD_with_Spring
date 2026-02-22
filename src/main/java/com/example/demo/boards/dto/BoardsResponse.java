package com.example.demo.boards.dto;

import java.time.LocalDateTime;

import com.example.demo.boards.domain.Boards;
import com.example.demo.users.domain.Users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "게시글 단일 응답")
@Getter
public class BoardsResponse {
	
	@Schema(description = "게시글 고유 아이디", example = "1")
	private Long id;
	
	@Schema(description = "게시글 제목", example = "게시글 제목입니다")
	private String title;
	
	@Schema(description = "게시글 내용", example = "게시글 내용입니다")
	private String content;
	
	@Schema(description = "게시글 생성일", example = "YYYY-MM-DD HH:MM:SS")
	private LocalDateTime createdAt;
	
	@Schema(description = "게시글 수정일", example = "YYYY-MM-DD HH:MM:SS")
	private LocalDateTime updatedAt;
	
	@Schema(description = "게시글 작성자 정보", example = "{id, nickname, password}")
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
