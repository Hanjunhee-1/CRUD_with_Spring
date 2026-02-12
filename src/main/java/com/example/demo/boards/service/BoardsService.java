package com.example.demo.boards.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.boards.domain.Boards;
import com.example.demo.boards.dto.BoardsFilterRequest;
import com.example.demo.boards.dto.BoardsRequest;
import com.example.demo.boards.dto.BoardsResponse;
import com.example.demo.boards.dto.BoardsResponseWithPage;
import com.example.demo.boards.repository.BoardsRepository;
import com.example.demo.boards.repository.BoardsSpecification;
import com.example.demo.users.domain.Users;
import com.example.demo.users.repository.UsersRepository;
import com.example.demo.util.pages.PageDto;

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
	
	public BoardsResponseWithPage getAllBoard(BoardsFilterRequest boardsFilter, PageDto pagination) {
		Specification<Boards> spec = (root, query, cb) -> cb.conjunction();
		
		if (boardsFilter.hasTitle()) spec = spec.and(BoardsSpecification.titleLike(boardsFilter.getTitle()));
		
		if (boardsFilter.hasNickname()) spec = spec.and(BoardsSpecification.nicknameLike(boardsFilter.getNickname()));
		
		Pageable page = PageRequest.of(pagination.getPageNumber(), pagination.getPageSize(), boardsFilter.toSort() == null ? Sort.unsorted() : boardsFilter.toSort());
		
		Page<Boards> boards = boardsRepository.findAll(spec, page);
		
		return new BoardsResponseWithPage(boards);
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
