package com.community.backend.domain.post.dto.request;

import com.community.backend.common.dto.CommonPagingRequestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentPagingRequestDto extends CommonPagingRequestDto {

	public PostCommentPagingRequestDto() {
		super();
	}
}
