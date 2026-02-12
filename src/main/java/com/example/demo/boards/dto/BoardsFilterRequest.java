package com.example.demo.boards.dto;

import org.springframework.data.domain.Sort;

import com.example.demo.util.enums.SortDirection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardsFilterRequest {
	private String title;
	private String nickname;
	private String createdAt;
	private String updatedAt;
	
	public BoardsFilterRequest() {}
	
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
