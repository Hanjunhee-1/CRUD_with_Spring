package com.example.demo.boards.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.boards.dto.BoardsRequest;
import com.example.demo.boards.dto.BoardsResponse;
import com.example.demo.boards.service.BoardsService;
import com.example.demo.users.domain.Users;

/**
 * 
 * description
 * 
 * what I need to develop?
 * 
 * -> CRUD
 * - getAllBoard: It returns boards. later add filtering.
 */

@RestController()
@RequestMapping("/boards")
public class BoardsController {
	private final BoardsService boardsService;
	
	public BoardsController(BoardsService boardsService) {
		this.boardsService = boardsService;
	}
	
	@PostMapping()
	public ResponseEntity<BoardsResponse> createBoard(
			Authentication authentication,
			@RequestBody BoardsRequest createBoardsRequest
		) {
		Users user = (Users) authentication.getPrincipal();
		BoardsResponse boardsResponse = this.boardsService.createBoard(user.getId(), createBoardsRequest);
		return ResponseEntity.ok(boardsResponse);
	}
	
	@GetMapping()
	public ResponseEntity<List<BoardsResponse>> getAllBoard() {
		List<BoardsResponse> boardsResponse = this.boardsService.getAllBoard();
		return ResponseEntity.ok(boardsResponse);
	}
	
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
