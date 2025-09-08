package com.community.backend.domain.payment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.community.backend.common.exception.BaseException;
import com.community.backend.domain.member.entity.Member;
import com.community.backend.domain.member.repository.MemberRepository;
import com.community.backend.domain.member.service.MemberService;
import com.community.backend.domain.payment.dto.response.PaymentDetailResponseDto;
import com.community.backend.domain.payment.dto.response.PaymentResponseDto;
import com.community.backend.domain.payment.entity.ChargeHistory;
import com.community.backend.domain.payment.exception.PaymentExceptionEnum;
import com.community.backend.domain.payment.repository.PaymentJpaRepository;
import com.community.backend.domain.payment.webclient.PaymentWebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	@Value("${iamport.api.key}")
	private String iamportApiKey;

	@Value("${iamport.secret}")
	private String iamportSecret;

	private final PaymentWebClient paymentWebClient;
	private final PaymentJpaRepository paymentJpaRepository;
	private final MemberService memberService;

	@Override
	@Transactional
	public PaymentResponseDto<PaymentDetailResponseDto> getPaymentDetail(String impUid, Long memberIdx) {

		String token = paymentWebClient.getToken(iamportApiKey, iamportSecret).response().access_token();
		Member member = memberService.getMemberEntityById(memberIdx);

		if(token == null){
			throw new BaseException(PaymentExceptionEnum.TOKEN_NOT_FOUND);
		}

		PaymentResponseDto<PaymentDetailResponseDto> result = paymentWebClient.getPaymentDetail(impUid, token);

		if(result == null){
			throw new BaseException(PaymentExceptionEnum.PAYMENT_NOT_FOUND);
		}

		ChargeHistory history = ChargeHistory.of(member, result.response().amount(), result.response().pay_method());
		paymentJpaRepository.save(history);

		return result;
	}
}
