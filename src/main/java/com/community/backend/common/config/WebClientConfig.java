package com.community.backend.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class WebClientConfig {

	@Value("${webclient.payment.baseurl}")
	private String paymentUrl;

	private final String baseUrl = "http://localhost:8080";

	@Bean("iamportWebClient")
	public WebClient webClient() {

		return WebClient.builder()
				.baseUrl(paymentUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	@Bean("jwtWebClient")
	public WebClient jwtWebClient() {

		return WebClient.builder()
				.baseUrl(baseUrl)
				.filter((request, next) -> {
					// 현재 쓰레드의 HttpServletRequest 가져오기
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest servletRequest = attrs.getRequest();
						String token = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
						if (token != null) {
							// Authorization 헤더 자동 주입
							return next.exchange(
									ClientRequest.from(request)
											.header(HttpHeaders.AUTHORIZATION, token)
											.build());
						}
					}
					return next.exchange(request);
				})
				.build();
	}
}
