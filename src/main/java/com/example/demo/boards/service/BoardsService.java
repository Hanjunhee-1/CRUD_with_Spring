package com.example.demo.boards.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.boards.domain.Boards;
import com.example.demo.boards.dto.BoardsRequest;
import com.example.demo.boards.dto.BoardsResponse;
import com.example.demo.boards.repository.BoardsRepository;
import com.example.demo.users.domain.Users;
import com.example.demo.users.repository.UsersRepository;

@Service
public class BoardsService {
	private final BoardsRepository boardsRepository;
	private final UsersRepository usersRepository;
	
	public BoardsService(
			BoardsRepository boardsRepository,
			UsersRepository usersRepository
		) {
		this.boardsRepository = boardsRepository;
		this.usersRepository = usersRepository;
	}
	
	public BoardsResponse createBoard(Long userId, BoardsRequest createBoardsRequest) {
		Optional<Users> foundUser = usersRepository.findById(userId);
		if (foundUser == null) {
			throw new RuntimeException("User not found");
		}
		
		if (createBoardsRequest.getTitle().isEmpty() || createBoardsRequest.getContent().isEmpty()) {
			throw new RuntimeException("title or content shouldn't be empty");
		}
		
		Boards board = new Boards();
		board.setTitle(createBoardsRequest.getTitle());
		board.setContent(createBoardsRequest.getContent());
		board.setUser(foundUser.get());
		
		return BoardsResponse.withUser(boardsRepository.save(board));
	}
	
	public List<BoardsResponse> getAllBoard() {
		return boardsRepository.findAll().stream()
				.map(board -> BoardsResponse.withUser(board))
				.toList();
	}
	
	public BoardsResponse updateBoard(Long userId, Long boardId, BoardsRequest updateBoardsRequest) {
		Optional<Boards> foundBoard = boardsRepository.findById(boardId);
		if (foundBoard == null) {
			throw new RuntimeException("Post not found");
		}
		
		if (!usersRepository.existsById(userId)) {
			throw new RuntimeException("User not found");
		}
		
		if (foundBoard.get().getUser().getId() != userId) {
			throw new RuntimeException("No authority");
		}
		
		foundBoard.get().setTitle(
				updateBoardsRequest.getTitle() == null || updateBoardsRequest.getTitle().isEmpty()
				? foundBoard.get().getTitle()
				: updateBoardsRequest.getTitle());
		foundBoard.get().setContent(
				updateBoardsRequest.getContent() == null || updateBoardsRequest.getContent().isEmpty()
				? foundBoard.get().getContent()
				: updateBoardsRequest.getContent());

		LocalDateTime currentDate = LocalDateTime.now();
		foundBoard.get().setUpdatedAt(
					foundBoard.get().getUpdatedAt() == null || foundBoard.get().getUpdatedAt().isBefore(currentDate)
					? currentDate
					: foundBoard.get().getUpdatedAt());
		
		return BoardsResponse.withUser(boardsRepository.save(foundBoard.get()));
	}
	
	public void deleteBoard(Long userId, Long boardId) {
		Optional<Boards> foundBoard = boardsRepository.findById(boardId);
		if (foundBoard == null) {
			throw new RuntimeException("Post not found");
		}
		
		if (!usersRepository.existsById(userId)) {
			throw new RuntimeException("User not found");
		}
		
		if (foundBoard.get().getUser().getId() != userId) {
			throw new RuntimeException("No authority");
		}
		
		boardsRepository.delete(foundBoard.get());
	}
}
