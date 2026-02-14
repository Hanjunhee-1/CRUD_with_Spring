package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 사용자 정의 exception 필터
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ExceptionResponse> handleApiException(ApiException ae) {
		ExceptionResponse er = new ExceptionResponse(ae.getExceptionCode());
		
		return ResponseEntity
				.status(ae.getExceptionCode().getCode())
				.body(er);
	}
	
	// 예상 못한 오류 필터
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception e) {
		ExceptionResponse er = new ExceptionResponse(ExceptionCode.INTERNAL_SERVER_ERROR);
		
		return ResponseEntity.status(ExceptionCode.INTERNAL_SERVER_ERROR.getCode()).body(er);
	}
}
