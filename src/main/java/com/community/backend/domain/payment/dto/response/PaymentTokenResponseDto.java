package com.community.backend.domain.payment.dto.response;

public record PaymentTokenResponseDto(
	String access_token,
	Integer expired_at,
	Integer now
) {
}
