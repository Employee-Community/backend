package com.community.backend.domain.payment.dto.response;

public record PaymentResponseDto<T>(
	Integer code,
	String message,
	T response
) {
}
