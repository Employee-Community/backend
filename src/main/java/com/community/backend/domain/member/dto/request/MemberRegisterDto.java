package com.community.backend.domain.member.dto.request;

import com.community.backend.domain.member.enums.MemberRole;

public record MemberRegisterDto(
	String name,
	String id,
	String password,
	String email,
	String phone,
	String nickname,
	MemberRole role
) {
}
