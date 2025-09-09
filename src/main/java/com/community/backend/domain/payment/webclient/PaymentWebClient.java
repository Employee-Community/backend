package com.community.backend.domain.payment.webclient;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.community.backend.domain.payment.dto.request.TokenRequestDto;
import com.community.backend.domain.payment.dto.response.PaymentDetailResponseDto;
import com.community.backend.domain.payment.dto.response.PaymentResponseDto;
import com.community.backend.domain.payment.dto.response.PaymentTokenResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentWebClient {

	private final WebClient webClient;

	public PaymentWebClient(@Qualifier("iamportWebClient") WebClient webClient) {
		this.webClient = webClient;
	}

	public PaymentResponseDto<PaymentTokenResponseDto> getToken(String key, String secret) {

		TokenRequestDto body = new TokenRequestDto(key, secret);

		try{
			return webClient
				.post()
				.uri("/users/getToken")
				.bodyValue(body)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<PaymentResponseDto<PaymentTokenResponseDto>>() {})
				.block();
		}catch(Exception e){
			log.error(e.getMessage());
			return null;
		}
	}

	public PaymentResponseDto<PaymentDetailResponseDto> getPaymentDetail(String impUid, String iamAccessToken) {

		try{
			return webClient
				.get()
				.uri("/payments/" + impUid)
				.header("Authorization", "Bearer " + iamAccessToken)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<PaymentResponseDto<PaymentDetailResponseDto>>() {})
				.block();
		}catch(Exception e){
			log.error(e.getMessage());
			return null;
		}
	}
}
