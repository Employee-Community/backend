package com.community.backend.common.security.jwt;

public record JwtPayload(
	Long idx,
	String email,
	String nickname
) {
}
