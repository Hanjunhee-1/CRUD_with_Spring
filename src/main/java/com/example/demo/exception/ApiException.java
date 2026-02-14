package com.example.demo.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

	private final ExceptionCode exceptionCode;
	
	public ApiException(ExceptionCode exceptionCode) {
		super(exceptionCode.getMessage());
		this.exceptionCode = exceptionCode;
	}
}
