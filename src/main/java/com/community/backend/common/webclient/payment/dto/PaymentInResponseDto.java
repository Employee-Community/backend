package com.community.backend.common.webclient.payment.dto;

public record PaymentInResponseDto(
	Long idx,
	Long memberIdx,
	String impUid,
	Integer amount,
	String paymentType
) {
}
