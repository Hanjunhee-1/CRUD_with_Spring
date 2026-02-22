package com.example.demo.boards.dto;

import org.springframework.data.domain.Sort;

import com.example.demo.util.enums.SortDirection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "게시글 조회 요청(필터링)")
@Getter
@Setter
public class BoardsFilterRequest {
	
	@Schema(description = "게시글 고유 아이디", example = "1")
	private Long boardId;
	
	@Schema(description = "게시글 제목", example = "test2 가 작성한 게시글")
	private String title;
	
	@Schema(description = "사용자 닉네임", example = "test2")
	private String nickname;
	
	@Schema(description = "생성일 기준 정렬", example = "[ASC, DESC]")
	private String createdAt;
	
	@Schema(description = "수정일 기준 정렬", example = "[ASC, DESC]")
	private String updatedAt;
	
	public BoardsFilterRequest() {}
	
	public boolean hasId() {
		return boardId != null;
	}
	
	public boolean hasTitle() {
		return title != null && !title.trim().isEmpty();
	}

	public boolean hasNickname() {
		return nickname != null && !nickname.trim().isEmpty();
	}
	
	public boolean hasCreatedAtSort() {
		return SortDirection.ASC.name().equalsIgnoreCase(createdAt) || SortDirection.DESC.name().equalsIgnoreCase(createdAt);
	}
	
	public boolean hasUpdatedAtSort() {
		return SortDirection.ASC.name().equalsIgnoreCase(updatedAt) || SortDirection.DESC.name().equalsIgnoreCase(updatedAt);
	}
	
    public Sort toSort() {
        Sort sort = Sort.unsorted();

        if (hasCreatedAtSort()) {
            sort = sort.and(Sort.by(
                    "asc".equalsIgnoreCase(createdAt) ? Sort.Direction.ASC : Sort.Direction.DESC,
                    "createdAt"
            ));
        }

        if (hasUpdatedAtSort()) {
            sort = sort.and(Sort.by(
                    "asc".equalsIgnoreCase(updatedAt) ? Sort.Direction.ASC : Sort.Direction.DESC,
                    "updatedAt"
            ));
        }

        return sort;
    }
}
