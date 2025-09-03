package com.community.backend.domain.member.service;

import com.community.backend.domain.member.dto.request.MemberLogInDto;
import com.community.backend.domain.member.dto.request.MemberRegisterDto;
import com.community.backend.domain.member.dto.response.MemberResponseDto;

public interface MemberService {

	void registerMember(MemberRegisterDto request);

	void deleteMember(Long idx, String password);

	MemberResponseDto logInMember(MemberLogInDto request);

	MemberResponseDto getMemberByIdx(Long idx);
}
