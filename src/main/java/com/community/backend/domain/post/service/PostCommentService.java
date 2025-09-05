package com.community.backend.domain.post.service;

import java.util.List;

import com.community.backend.common.dto.CommonPagingResponseDto;
import com.community.backend.domain.post.dto.request.PostCommentPagingRequestDto;
import com.community.backend.domain.post.dto.request.PostCommentRequestDto;
import com.community.backend.domain.post.dto.request.PostCommentUpdateRequestDto;
import com.community.backend.domain.post.dto.response.PostCommentResponseDto;

public interface PostCommentService {

	void createPostComment(PostCommentRequestDto request, Long memberIdx);

	void updatePostComment(PostCommentUpdateRequestDto request, Long memberIdx);

	void deletePostComment(Long commentIdx, Long memberIdx);

	CommonPagingResponseDto<List<PostCommentResponseDto>>
		getPostComments(Long postIdx, PostCommentPagingRequestDto request);

	CommonPagingResponseDto<List<PostCommentResponseDto>>
		getMyPostComments(PostCommentPagingRequestDto request, Long memberIdx);
}
