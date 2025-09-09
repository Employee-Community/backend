package com.community.backend.domain.member.dto.request;

import com.community.backend.domain.member.enums.MemberRole;

public record MembershipChangeDto(
	MemberRole role,
	String impUid
) {
}
