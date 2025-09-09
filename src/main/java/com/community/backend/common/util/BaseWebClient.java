package com.community.backend.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.community.backend.common.dto.ApiResponse;

import reactor.core.publisher.Mono;

@Component
public class BaseWebClient {

    private final WebClient baseWebClient;

    public BaseWebClient(@Qualifier("jwtWebClient") WebClient baseWebClient) {
        this.baseWebClient = baseWebClient;
    }

    public WebClient getWebClient() {
        return baseWebClient;
    }

    public <T> Mono<ResponseEntity<ApiResponse<T>>> post(String uri, Object body) {
        return baseWebClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<ApiResponse<T>>() {
                });
    }

    public <T> Mono<ResponseEntity<ApiResponse<T>>> post(String uri, Object body, MediaType contentType) {
        return baseWebClient.post()
                .uri(uri)
                .contentType(contentType)
                .bodyValue(body)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<ApiResponse<T>>() {
                });
    }

    public <T> Mono<ResponseEntity<ApiResponse<T>>> post(String uri, Map<String, String> headers, Object body) {
        return baseWebClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders
                        .setAll(headers == null || headers.isEmpty() ? new HashMap<String, String>() : headers))
                .bodyValue(body)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<ApiResponse<T>>() {
                });
    }

    public <T> Mono<ResponseEntity<ApiResponse<T>>> post(String uri, Map<String, String> headers, Object body,
            MediaType contentType) {
        return baseWebClient.post()
                .uri(uri)
                .contentType(contentType)
                .headers(httpHeaders -> httpHeaders
                        .setAll(headers == null || headers.isEmpty() ? new HashMap<String, String>() : headers))
                .bodyValue(body)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<ApiResponse<T>>() {
                });
    }

    public <T> Mono<ResponseEntity<ApiResponse<T>>> get(String uri) {
        return baseWebClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<ApiResponse<T>>() {
                });
    }
}
