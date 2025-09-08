package com.community.backend.domain.payment.dto.response;

public record PaymentDetailResponseDto(
	String imp_uid,
	String merchant_uid,
	String status,
	int amount,
	String pay_method,
	String buyer_name,
	String buyer_email,
	String buyer_tel
) {
}
