package com.community.backend.domain.member.repository;

import java.util.Optional;

import com.community.backend.domain.member.dto.response.MemberResponseDto;
import com.community.backend.domain.member.entity.Member;

public interface MemberRepository {

	Optional<Member> getMemberByEmail(String email);

	void registerMember(Member member);

	Optional<Member> getMemberByIdx(Long idx);

	Optional<Member> getMemberById(String id);
}
