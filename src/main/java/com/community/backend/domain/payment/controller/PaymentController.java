package com.community.backend.domain.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.community.backend.common.dto.ApiResponse;
import com.community.backend.common.security.jwt.JwtPayload;
import com.community.backend.domain.payment.dto.request.PaymentVerifyRequestDto;
import com.community.backend.domain.payment.dto.response.PaymentDetailResponseDto;
import com.community.backend.domain.payment.dto.response.PaymentResponseDto;
import com.community.backend.domain.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/verify")
	ResponseEntity<ApiResponse<PaymentResponseDto<PaymentDetailResponseDto>>> verifyPayment(
		@RequestBody PaymentVerifyRequestDto paymentVerifyRequestDto,
		@AuthenticationPrincipal JwtPayload payload) {

		PaymentResponseDto<PaymentDetailResponseDto> response = paymentService
			.getPaymentDetail(paymentVerifyRequestDto.impUid(), payload.idx());

		return ResponseEntity.ok(ApiResponse.success("결제가 성공하였습니다.", response));
	}
}
