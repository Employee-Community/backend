package com.community.backend.domain.post.dto.response;

import java.time.LocalDateTime;

import com.community.backend.domain.post.entity.PostComment;

public record PostCommentResponseDto(
	Long idx,
	Long postIdx,
	Long memberIdx,
	String content,
	Integer state,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

	public static PostCommentResponseDto from(PostComment postComment) {
		return new PostCommentResponseDto(
			postComment.getIdx(),
			postComment.getPost().getIdx(),
			postComment.getMember().getIdx(),
			postComment.getContent(),
			postComment.getState(),
			postComment.getCreatedAt(),
			postComment.getUpdatedAt()
		);
	}
}
