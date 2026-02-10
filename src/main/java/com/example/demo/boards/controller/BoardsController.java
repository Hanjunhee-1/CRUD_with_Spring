package com.example.demo.boards.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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

/**
 * 
 * description
 * 
 * what I need to develop?
 * 
 * -> CRUD
 * - createBoard: It creates board. after jwt added, remove pathVariable.
 * - getAllBoard: It returns boards. later add filtering.
 * - updateBoard: It updates board.
 * - deleteBoard: It deletes board. if there will be admin, add deleting all boards.
 */

@RestController()
@RequestMapping("/boards")
public class BoardsController {
	private final BoardsService boardsService;
	
	public BoardsController(BoardsService boardsService) {
		this.boardsService = boardsService;
	}
	
	@PostMapping("/{userId}")
	public ResponseEntity<BoardsResponse> createBoard(
			@PathVariable("userId") Long userId,
			@RequestBody BoardsRequest createBoardsRequest
		) {
		BoardsResponse boardsResponse = this.boardsService.createBoard(userId, createBoardsRequest);
		return ResponseEntity.ok(boardsResponse);
	}
	
	@GetMapping("")
	public ResponseEntity<List<BoardsResponse>> getAllBoard() {
		List<BoardsResponse> boardsResponse = this.boardsService.getAllBoard();
		return ResponseEntity.ok(boardsResponse);
	}
	
	@PatchMapping("/{userId}/{boardId}")
	public ResponseEntity<BoardsResponse> updateBoard(
			@PathVariable("userId") Long userId,
			@PathVariable("boardId") Long boardId,
			@RequestBody BoardsRequest updateBoardsRequest
		) {
		BoardsResponse boardsResponse = this.boardsService.updateBoard(userId, boardId, updateBoardsRequest);
		return ResponseEntity.ok(boardsResponse);
	}
	
	@DeleteMapping("/{userId}/{boardId}")
	public ResponseEntity<Void> deleteBoard(
			@PathVariable("userId") Long userId,
			@PathVariable("boardId") Long boardId
		) {
		this.boardsService.deleteBoard(userId, boardId);
		return ResponseEntity.noContent().build();
	}
}
