package com.community.backend.common.webclient.payment;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.community.backend.common.dto.ApiResponse;
import com.community.backend.common.webclient.payment.dto.PaymentInResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentInternalWebclient {

	private final WebClient webClient;

	public PaymentInternalWebclient(@Qualifier("internalWebclient") WebClient webClient) {
		this.webClient = webClient;
	}

	public PaymentInResponseDto confirmPayment(String impUid) {

		try{

			ApiResponse<PaymentInResponseDto> response =  webClient
				.get()
				.uri("/v1/payment/{impUid}", impUid)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<ApiResponse<PaymentInResponseDto>>() {
				})
				.block();

			if(response == null) {
				return null;
			}

			return response.getData();
		}catch(Exception e){

			log.error(e.getMessage());
			return null;
		}
	}
}
