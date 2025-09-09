package com.community.backend.domain.member.service;

import com.community.backend.domain.member.dto.request.MemberLogInDto;
import com.community.backend.domain.member.dto.request.MemberRegisterDto;
import com.community.backend.domain.member.dto.request.MembershipChangeDto;
import com.community.backend.domain.member.dto.response.MemberResponseDto;
import com.community.backend.domain.member.entity.Member;
import com.community.backend.domain.member.enums.MemberRole;

public interface MemberService {

	Member getMemberEntityById(Long memberIdx);

	void registerMember(MemberRegisterDto request);

	void deleteMember(Long idx, String password);

	MemberResponseDto logInMember(MemberLogInDto request);

	MemberResponseDto getMemberByIdx(Long idx);

	void changeMembership(Long memberIdx, MembershipChangeDto request);
}
