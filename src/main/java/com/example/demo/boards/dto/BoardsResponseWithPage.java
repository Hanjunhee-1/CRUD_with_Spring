package com.example.demo.boards.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.boards.domain.Boards;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "게시글 다중 응답")
@Getter
@Setter
public class BoardsResponseWithPage {
	
	@Schema(description = "게시글 정보 모음", example = "{id, title, content, createdAt, updatedAt, user: {id, nickname, password}}")
	private List<BoardsResponse> contents;
	
	@Schema(description = "페이지 번호", example = "0")
	private Integer pageNumber = 0;
	
	@Schema(description = "페이지 크기", example = "10")
	private Integer pageSize = 0;
	
	@Schema(description = "페이지 개수", example = "1")
	private Integer totalPages = 0;
	
	@Schema(description = "조회된 게시글 수", example = "11")
	private Long totalElements = 0L;
	
	public BoardsResponseWithPage() {}
	
	public BoardsResponseWithPage(Page<Boards> boards) {
		this.contents = boards.getContent().stream()
							.map(board -> BoardsResponse.withUser(board))
							.toList();
		this.pageNumber = boards.getNumber();
		this.pageSize = boards.getSize();
		this.totalPages = boards.getTotalPages();
		this.totalElements = boards.getTotalElements();
	}
	
	public BoardsResponseWithPage(Boards board) {
		this.contents = List.of(BoardsResponse.withUser(board));
		this.pageSize = 1;
		this.totalPages = 1;
		this.totalElements = 1L;
	}
}
