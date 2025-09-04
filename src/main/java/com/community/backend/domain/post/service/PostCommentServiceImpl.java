package com.community.backend.domain.post.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.community.backend.common.dto.CommonPagingResponseDto;
import com.community.backend.domain.member.entity.Member;
import com.community.backend.domain.member.service.MemberService;
import com.community.backend.domain.post.dto.request.MyPostCommentPagingRequestDto;
import com.community.backend.domain.post.dto.request.PostCommentPagingRequestDto;
import com.community.backend.domain.post.dto.request.PostCommentRequestDto;
import com.community.backend.domain.post.dto.request.PostCommentUpdateRequestDto;
import com.community.backend.domain.post.dto.response.PostCommentResponseDto;
import com.community.backend.domain.post.entity.Post;
import com.community.backend.domain.post.entity.PostComment;
import com.community.backend.domain.post.repository.PostCommentJpaRepository;
import com.community.backend.domain.post.repository.PostJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

	private final PostCommentJpaRepository postCommentJpaRepository;
	private final MemberService memberService;
	private final PostJpaRepository postJpaRepository;

	/**
	 * 댓글 작성 메서드 / 예외 처리 부분은 PostEnum이 추가되면 추가 작성 예정
	 */
	@Transactional
	public void createPostComment(PostCommentRequestDto request, Long memberIdx) {

		Member member = memberService.getMemberEntityById(memberIdx);

		// TODO : 예외 처리 통일 필요
		Post post = postJpaRepository.findById(request.postIdx()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		PostComment postComment = new PostComment(null, member, post, request.content(), 1);

		postCommentJpaRepository.save(postComment);
	}

	@Transactional
	public void updatePostComment(PostCommentUpdateRequestDto request, Long memberIdx) {

		Member member = memberService.getMemberEntityById(memberIdx);

		// TODO : 예외 처리 통일 필요
		PostComment postComment = postCommentJpaRepository.findById(request.idx()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(!Objects.equals(member.getIdx(), postComment.getMember().getIdx())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		postComment.updatePostComment(request.content());
	}

	@Transactional
	public void deletePostComment(Long commentIdx, Long memberIdx) {

		Member member = memberService.getMemberEntityById(memberIdx);

		// TODO : 예외 처리 통일 필요
		PostComment postComment = postCommentJpaRepository.findById(commentIdx).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(!Objects.equals(member.getIdx(), postComment.getMember().getIdx())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		postCommentJpaRepository.delete(postComment);
	}

	@Transactional(readOnly = true)
	public CommonPagingResponseDto<List<PostCommentResponseDto>>
		getPostComments(PostCommentPagingRequestDto request) {

		request.clean();

		// TODO 예외 처리
		if(postJpaRepository.findById(request.getPostIdx()).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.Direction.DESC, "createdAt");
		List<PostCommentResponseDto> postComments = postCommentJpaRepository
			.findByPost_Idx(request.getPostIdx(), pageable)
			.stream()
			.map(PostCommentResponseDto::from)
			.toList();

		return new CommonPagingResponseDto<>(request, postComments);
	}

	@Transactional(readOnly = true)
	public CommonPagingResponseDto<List<PostCommentResponseDto>>
		getMyPostComments(MyPostCommentPagingRequestDto request, Long memberIdx) {

		request.clean();

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
		List<PostCommentResponseDto> postComments = postCommentJpaRepository
			.findByMemberIdx(memberIdx, pageable)
			.stream()
			.map(PostCommentResponseDto::from)
			.toList();

		return new CommonPagingResponseDto<>(request, postComments);
	}
}
