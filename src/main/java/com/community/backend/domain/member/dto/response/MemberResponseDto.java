package com.community.backend.domain.member.dto.response;

import java.time.LocalDateTime;

import com.community.backend.domain.member.entity.Member;
import com.community.backend.domain.member.enums.LogInType;
import com.community.backend.domain.member.enums.MemberRole;

public record MemberResponseDto(
	Long idx,
	LogInType loginType,
	String name,
	String id,
	String email,
	String phone,
	String nickname,
	MemberRole role,
	Integer state,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

	public static MemberResponseDto fromMember(Member member) {
		return new MemberResponseDto(
			member.getIdx(),
			member.getLoginType(),
			member.getName(),
			member.getId(),
			member.getEmail(),
			member.getPhone(),
			member.getNickname(),
			member.getRole(),
			member.getState(),
			member.getCreatedAt(),
			member.getUpdatedAt()
		);
	}
}
