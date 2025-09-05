package com.community.backend.domain.post.dto.request;

public record PostCommentRequestDto(
	Long postIdx,
	String content
) {
}
