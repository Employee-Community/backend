package com.community.backend.domain.post.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.community.backend.common.dto.ApiResponse;
import com.community.backend.common.dto.CommonPagingResponseDto;
import com.community.backend.common.security.jwt.JwtPayload;
import com.community.backend.domain.post.dto.request.MyPostCommentPagingRequestDto;
import com.community.backend.domain.post.dto.request.PostCommentPagingRequestDto;
import com.community.backend.domain.post.dto.request.PostCommentRequestDto;
import com.community.backend.domain.post.dto.request.PostCommentUpdateRequestDto;
import com.community.backend.domain.post.dto.response.PostCommentResponseDto;
import com.community.backend.domain.post.service.PostCommentService;
import com.community.backend.domain.post.service.PostCommentServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/post/comment")
@RequiredArgsConstructor
public class PostCommentController {

	private final PostCommentService postCommentService;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createPostComment(
		@RequestBody PostCommentRequestDto request,
		@AuthenticationPrincipal JwtPayload jwtPayload) {

		postCommentService.createPostComment(request, jwtPayload.idx());
		return ResponseEntity.ok(ApiResponse.success("댓글 등록이 성공적으로 완료되었습니다", null));
	}

	@PatchMapping
	public ResponseEntity<ApiResponse<Void>> updatePostComment(
		@RequestBody PostCommentUpdateRequestDto request,
		@AuthenticationPrincipal JwtPayload jwtPayload) {

		postCommentService.updatePostComment(request, jwtPayload.idx());
		return ResponseEntity.ok(ApiResponse.success("댓글 수정이 성공적으로 완료되었습니다", null));
	}

	@DeleteMapping("/{commentIdx}")
	public ResponseEntity<ApiResponse<Void>> deletePostComment(
		@PathVariable Long commentIdx,
		@AuthenticationPrincipal JwtPayload jwtPayload) {

		postCommentService.deletePostComment(commentIdx, jwtPayload.idx());
		return ResponseEntity.ok(ApiResponse.success("댓글 삭제가 성공적으로 완료되었습니다", null));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<CommonPagingResponseDto<List<PostCommentResponseDto>>>> getPostComments(
		@ModelAttribute PostCommentPagingRequestDto request) {

		return ResponseEntity.ok(ApiResponse
			.success("댓글 목록 조회가 성공적으로 완료되었습니다",
				postCommentService.getPostComments(request)));
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<CommonPagingResponseDto<List<PostCommentResponseDto>>>> getMyPostComments(
		@ModelAttribute MyPostCommentPagingRequestDto request,
		@AuthenticationPrincipal JwtPayload jwtPayload) {

		return ResponseEntity.ok(ApiResponse
			.success("본인이 작성한 댓글 목록 조회가 성공적으로 완료되었습니다.",
				postCommentService.getMyPostComments(request, jwtPayload.idx())));
	}
}
