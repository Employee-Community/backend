package com.community.backend.domain.post.dto.request;

public record PostCommentUpdateRequestDto(
	Long idx,
	Long postIdx,
	String content
) {
}
