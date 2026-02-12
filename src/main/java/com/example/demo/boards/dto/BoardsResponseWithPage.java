package com.example.demo.boards.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.boards.domain.Boards;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardsResponseWithPage {
	private List<BoardsResponse> content;
	private Integer pageNumber = 0;
	private Integer pageSize = 0;
	private Integer totalPages = 0;
	private Long totalElements = 0L;
	
	public BoardsResponseWithPage() {}
	
	public BoardsResponseWithPage(Page<Boards> boards) {
		this.content = boards.getContent().stream()
							.map(board -> BoardsResponse.withUser(board))
							.toList();
		this.pageNumber = boards.getNumber();
		this.pageSize = boards.getSize();
		this.totalPages = boards.getTotalPages();
		this.totalElements = boards.getTotalElements();
	}
}
