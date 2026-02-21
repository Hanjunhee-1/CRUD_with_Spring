package com.example.demo.boards.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.boards.dto.BoardsFilterRequest;
import com.example.demo.boards.dto.BoardsRequest;
import com.example.demo.boards.dto.BoardsResponse;
import com.example.demo.boards.dto.BoardsResponseWithPage;
import com.example.demo.boards.service.BoardsService;
import com.example.demo.users.domain.Users;
import com.example.demo.util.pages.PageDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Boards", description = "게시판 관련 API")
@RestController()
@RequestMapping("/boards")
public class BoardsController {
	private final BoardsService boardsService;
	
	public BoardsController(BoardsService boardsService) {
		this.boardsService = boardsService;
	}
	
	@Operation(summary = "게시글 생성", description = "사용자로부터 정보(title, content) 를 입력받아 게시글을 생성합니다")
	@PostMapping()
	public ResponseEntity<BoardsResponse> createBoard(
			Authentication authentication,
			@RequestBody BoardsRequest createBoardsRequest
		) {
		Users user = (Users) authentication.getPrincipal();
		BoardsResponse boardsResponse = this.boardsService.createBoard(user.getId(), createBoardsRequest);
		return ResponseEntity.ok(boardsResponse);
	}
	
	@Operation(summary = "게시글 다중/단일 조회", description = "사용자로부터 정보(pageNumber, pageSize, title, nickname, createdAt or updatedAt [ASC, DESC], boardId) 를 입력받아 게시글을 다중/단일 조회합니다")
	@GetMapping()
	public ResponseEntity<BoardsResponseWithPage> getAllBoard(
			@ModelAttribute BoardsFilterRequest boardsFilter, 
			@ModelAttribute PageDto pagination
		) {
		BoardsResponseWithPage boardsResponse = this.boardsService.getAllBoard(boardsFilter, pagination);
		return ResponseEntity.ok(boardsResponse);
	}
	
	@Operation(summary = "게시글 수정", description = "사용자로부터 정보(boardId, title, content) 를 입력받아 게시글을 수정합니다")
	@PatchMapping("/{boardId}")
	public ResponseEntity<BoardsResponse> updateBoard(
			Authentication authentication,
			@PathVariable("boardId") Long boardId,
			@RequestBody BoardsRequest updateBoardsRequest
		) {
		Users user = (Users) authentication.getPrincipal();
		BoardsResponse boardsResponse = this.boardsService.updateBoard(user.getId(), boardId, updateBoardsRequest);
		return ResponseEntity.ok(boardsResponse);
	}
	
	@Operation(summary = "게시글 삭제", description = "사용자로부터 정보(boardId) 를 입력받아 게시글을 삭제합니다")
	@DeleteMapping("/{boardId}")
	public ResponseEntity<Void> deleteBoard(
			Authentication authentication,
			@PathVariable("boardId") Long boardId
		) {
		Users user = (Users) authentication.getPrincipal();
		this.boardsService.deleteBoard(user.getId(), boardId);
		return ResponseEntity.noContent().build();
	}
}
