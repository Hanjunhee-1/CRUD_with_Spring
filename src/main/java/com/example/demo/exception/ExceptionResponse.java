package com.example.demo.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {
	private final int code;
	private final String errorCode;
	private final String detailMessage;
	
	public ExceptionResponse(ExceptionCode exception) {
		this.code = exception.getCode();
		this.errorCode = exception.name();
		this.detailMessage = exception.getMessage();
	}
}
