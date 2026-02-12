package com.example.demo.util.pages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto {
	private Integer pageNumber = 0;
	private Integer pageSize = 10;
}
