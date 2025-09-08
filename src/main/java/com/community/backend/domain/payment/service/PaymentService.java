package com.community.backend.domain.payment.service;

import com.community.backend.domain.payment.dto.response.PaymentDetailResponseDto;
import com.community.backend.domain.payment.dto.response.PaymentResponseDto;

public interface PaymentService {

	PaymentResponseDto<PaymentDetailResponseDto> getPaymentDetail(String impUid, Long userIdx);
}
