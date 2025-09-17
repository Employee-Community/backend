package com.community.backend.common.kafka.dto.memberDto;

public record MemberRequestDto(
	Long memberIdx,
	String email
) {
}
