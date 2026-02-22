package com.example.demo.boards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "게시글 요청")
@Getter
@Setter
public class BoardsRequest {
	
	@Schema(description = "게시글 제목", example = "게시글 제목입니다")
	private String title;
	
	@Schema(description = "게시글 내용", example = "게시글 내용입니다")
	private String content;
}
