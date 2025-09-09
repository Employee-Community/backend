package com.community.backend.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.community.backend.common.security.jwt.JwtPayload;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class WebClientConfig {

	@Value("${webclient.payment.baseurl}")
	private String paymentUrl;

	@Value("${webclient.internal.baseurl}")
	private String internalBaseUrl;

	@Bean("iamportWebClient")
	public WebClient webClient() {

		return WebClient.builder()
			.baseUrl(paymentUrl)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}

	@Bean("internalWebclient")
	public WebClient internalWebclient() {

		ExchangeStrategies strategies = ExchangeStrategies.builder()
			.codecs(configurer -> {
				configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024); // 16MB
			})
			.build();

		return WebClient.builder()
			.baseUrl(internalBaseUrl)
			.exchangeStrategies(strategies)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.filter((request, next) -> {
				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest servletRequest = attrs.getRequest();
					String authHeader = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
					if (authHeader != null) {
						ClientRequest newRequest = ClientRequest.from(request)
							.header(HttpHeaders.AUTHORIZATION, authHeader)
							.build();
						return next.exchange(newRequest);
					}
				}
				return next.exchange(request);
			})
			.build();
	}
}
