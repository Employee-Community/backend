package com.community.backend.domain.post.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import com.community.backend.common.dto.CommonPagingResponseDto;
import com.community.backend.domain.mail.dto.MailSendRequestDto;
import com.community.backend.domain.member.entity.Member;
import com.community.backend.domain.member.service.MemberService;
import com.community.backend.domain.post.dto.request.PostCommentPagingRequestDto;
import com.community.backend.domain.post.dto.request.PostCommentRequestDto;
import com.community.backend.domain.post.dto.request.PostCommentUpdateRequestDto;
import com.community.backend.domain.post.dto.response.PostCommentResponseDto;
import com.community.backend.domain.post.entity.Post;
import com.community.backend.domain.post.entity.PostComment;
import com.community.backend.domain.post.repository.PostCommentJpaRepository;
import com.community.backend.domain.post.repository.PostJpaRepository;

import jakarta.servlet.http.HttpServletRequest;
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

		// 댓글 알림 이메일 발송 로직
		// TODO : 토큰 추출 로직 별도로 필요
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
        	HttpServletRequest servletRequest = attrs.getRequest();
        	String token = servletRequest.getHeader("Authorization");
			if (token == null || token.isEmpty()) {
				// 토큰이 없으면 이메일 발송하지 않음
				return;
			}

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", token);

			RestTemplate restTemplate = new RestTemplate();
			String url = "http://localhost:8080/v1/mail/send";
			MailSendRequestDto mailRequest = new MailSendRequestDto();
			mailRequest.setTo(member.getEmail());
			mailRequest.setSubject(String.format("[%s] 게시글 댓글 알림", post.getTitle()));
			mailRequest.setText(String.format("[JOBTALK]\n\n회원님이 작성하신 게시글 '%s'에 새로운 댓글이 작성되었습니다.\n\n댓글 내용: %s", post.getTitle(), request.content()));

			HttpEntity<MailSendRequestDto> entity = new HttpEntity<>(mailRequest, headers);
			try {
            	restTemplate.postForEntity(url, entity, Object.class);
        	} catch (Exception e) {
        	    e.printStackTrace();
        	}
		}
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
		getPostComments(Long postIdx, PostCommentPagingRequestDto request) {

		request.clean();

		// TODO 예외 처리
		if(postJpaRepository.findById(postIdx).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.Direction.DESC, "createdAt");
		List<PostCommentResponseDto> postComments = postCommentJpaRepository
			.findByPost_Idx(postIdx, pageable)
			.stream()
			.map(PostCommentResponseDto::from)
			.toList();

		return new CommonPagingResponseDto<>(request, postComments);
	}

	@Transactional(readOnly = true)
	public CommonPagingResponseDto<List<PostCommentResponseDto>>
		getMyPostComments(PostCommentPagingRequestDto request, Long memberIdx) {

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
