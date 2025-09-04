package com.community.backend.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.community.backend.common.exception.BaseException;
import com.community.backend.common.security.PasswordEncoder;
import com.community.backend.domain.member.dto.request.MemberLogInDto;
import com.community.backend.domain.member.dto.request.MemberRegisterDto;
import com.community.backend.domain.member.dto.response.MemberResponseDto;
import com.community.backend.domain.member.entity.Member;
import com.community.backend.domain.member.exception.MemberExceptionEnum;
import com.community.backend.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public void registerMember(MemberRegisterDto request) {

		if(memberRepository.getMemberByEmail(request.email()).isPresent()) {
			throw new BaseException(MemberExceptionEnum.DUPLICATE_EMAIL);
		}

		if(memberRepository.getMemberById(request.id()).isPresent()) {
			throw new BaseException(MemberExceptionEnum.DUPLICATE_ID);
		}

		Member member = Member.createDefault(request.name(), request.id(), passwordEncoder.encode(request.password()), request.email(),
			request.phone(), request.nickname(), request.role());

		memberRepository.registerMember(member);
	}

	@Override
	@Transactional
	public void deleteMember(Long idx, String password) {

		Member member = memberRepository.getMemberByIdx(idx).orElseThrow(
			() -> new BaseException(MemberExceptionEnum.MEMBER_NOT_FOUND)
		);

		if(!passwordEncoder.matches(password, member.getPassword())) {
			throw new BaseException(MemberExceptionEnum.INVALID_PASSWORD);
		}

		memberRepository.deleteMember(member);
	}

	@Override
	public MemberResponseDto logInMember(MemberLogInDto request) {

		Member member = memberRepository.getMemberById(request.id()).orElseThrow(
			() -> new BaseException(MemberExceptionEnum.MEMBER_NOT_FOUND)
		);

		if(!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new BaseException(MemberExceptionEnum.INVALID_PASSWORD);
		}

		return MemberResponseDto.fromMember(member);
	}

	@Override
	@Transactional(readOnly = true)
	public MemberResponseDto getMemberByIdx(Long idx) {

		Member member = memberRepository.getMemberByIdx(idx).orElseThrow(
			() -> new BaseException(MemberExceptionEnum.MEMBER_NOT_FOUND)
		);

		return MemberResponseDto.fromMember(member);
	}
}
