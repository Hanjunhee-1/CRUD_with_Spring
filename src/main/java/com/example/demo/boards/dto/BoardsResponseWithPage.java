package com.example.demo.boards.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.boards.domain.Boards;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardsResponseWithPage {
	private List<BoardsResponse> contents;
	private Integer pageNumber = 0;
	private Integer pageSize = 0;
	private Integer totalPages = 0;
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
