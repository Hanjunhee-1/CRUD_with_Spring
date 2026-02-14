package com.example.demo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
	/**
	 * 만약 새롭게 추가해야할 exception 이 있다면 아래에 모듈별로 추가해주면 됨.
	 */
	
	/** Auth **/
	INVALID_NICKNAME(400, "닉네임을 다시 확인해주세요"),
	NOT_FOUND_NICKNAME(401, "닉네임이 틀렸습니다"),
	INVALID_PASSWORD(400, "비밀번호를 다시 확인해주세요"),
	NOT_FOUND_PASSWORD(401, "비밀번호가 틀렸습니다"),
	
	/** Users **/
	NOT_FOUND_USER(401, "존재하지 않는 사용자입니다"),
	CONFLICT_NICKNAME(409, "해당 닉네임은 사용할 수 없습니다"),
	
	
	/** Boards **/
	INVALID_TITLE(400, "제목을 다시 확인해주세요"),
	INVALID_CONTENT(400, "내용을 다시 확인해주세요"),
	NOT_FOUND_POST(401, "존재하지 않는 게시글입니다"),
	FORBIDDEN_UPDATE_POST(403, "해당 게시글 수정 권한이 없습니다"),
	FORBIDDEN_DELETE_POST(403, "해당 게시글 삭제 권한이 없습니다"),
	
	/** 예상 못한 오류 **/
	INTERNAL_SERVER_ERROR(500, "서버에 오류가 발생했습니다");
	
	private final int code;
	private final String message;
}