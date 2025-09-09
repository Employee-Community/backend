package com.community.backend.domain.payment.dto.response;

import com.community.backend.domain.payment.entity.ChargeHistory;

public record ChargeHistoryResponseDto(
	Long idx,
	Long memberIdx,
	String impUid,
	Integer amount,
	String paymentType
) {

	public static ChargeHistoryResponseDto from(ChargeHistory chargeHistory) {

		return new ChargeHistoryResponseDto(
			chargeHistory.getIdx(),
			chargeHistory.getMember().getIdx(),
			chargeHistory.getImpUid(),
			chargeHistory.getAmount(),
			chargeHistory.getPaymentType()
		);
	}
}
