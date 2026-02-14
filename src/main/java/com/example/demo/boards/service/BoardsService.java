package com.example.demo.boards.service;

import java.time.LocalDateTime;

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
import com.example.demo.exception.ApiException;
import com.example.demo.exception.ExceptionCode;
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
		Users foundUser = usersRepository.findById(userId).orElse(null);
		if (foundUser == null) {
			throw new ApiException(ExceptionCode.NOT_FOUND_USER);
		}
		
		if (createBoardsRequest.getTitle() == null || createBoardsRequest.getTitle().isEmpty()) {
			throw new ApiException(ExceptionCode.INVALID_TITLE);
		}
		
		if (createBoardsRequest.getContent() == null || createBoardsRequest.getContent().isEmpty()) {
			throw new ApiException(ExceptionCode.INVALID_CONTENT);
		}
		
		Boards board = new Boards();
		board.setTitle(createBoardsRequest.getTitle());
		board.setContent(createBoardsRequest.getContent());
		board.setUser(foundUser);
		
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
		Boards foundBoard = boardsRepository.findById(boardId).orElse(null);
		if (foundBoard == null) {
			throw new ApiException(ExceptionCode.NOT_FOUND_POST);
		}
		
		if (!usersRepository.existsById(userId)) {
			throw new ApiException(ExceptionCode.NOT_FOUND_USER);
		}
		
		if (foundBoard.getUser().getId() != userId) {
			throw new ApiException(ExceptionCode.FORBIDDEN_UPDATE_POST);
		}
		
		foundBoard.setTitle(
				updateBoardsRequest.getTitle() == null || updateBoardsRequest.getTitle().isEmpty()
				? foundBoard.getTitle()
				: updateBoardsRequest.getTitle());
		foundBoard.setContent(
				updateBoardsRequest.getContent() == null || updateBoardsRequest.getContent().isEmpty()
				? foundBoard.getContent()
				: updateBoardsRequest.getContent());

		LocalDateTime currentDate = LocalDateTime.now();
		foundBoard.setUpdatedAt(
					foundBoard.getUpdatedAt() == null || foundBoard.getUpdatedAt().isBefore(currentDate)
					? currentDate
					: foundBoard.getUpdatedAt());
		
		return BoardsResponse.withUser(boardsRepository.save(foundBoard));
	}
	
	public void deleteBoard(Long userId, Long boardId) {
		Boards foundBoard = boardsRepository.findById(boardId).orElse(null);
		if (foundBoard == null) {
			throw new ApiException(ExceptionCode.NOT_FOUND_POST);
		}
		
		if (!usersRepository.existsById(userId)) {
			throw new ApiException(ExceptionCode.NOT_FOUND_USER);
		}
		
		if (foundBoard.getUser().getId() != userId) {
			throw new ApiException(ExceptionCode.FORBIDDEN_DELETE_POST);
		}
		
		boardsRepository.delete(foundBoard);
	}
}
