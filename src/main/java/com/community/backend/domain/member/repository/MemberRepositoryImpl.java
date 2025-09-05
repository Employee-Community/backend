package com.community.backend.domain.member.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.community.backend.domain.member.dto.response.MemberResponseDto;
import com.community.backend.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

	private final MemberJpaRepository jpaRepository;

	@Override
	public Optional<Member> getMemberByEmail(String email) {

		return jpaRepository.findByEmail(email);
	}

	@Override
	public void registerMember(Member member) {

		jpaRepository.save(member);
	}

	@Override
	public Optional<Member> getMemberByIdx(Long idx) {

		return jpaRepository.findById(idx);
	}

	@Override
	public Optional<Member> getMemberById(String id) {

		return jpaRepository.findById(id);
	}
}
