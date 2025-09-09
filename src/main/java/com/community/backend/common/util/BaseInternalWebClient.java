package com.community.backend.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.community.backend.common.dto.ApiResponse;

import reactor.core.publisher.Mono;

@Component
public class BaseInternalWebClient {

    private final WebClient baseWebClient;

    public BaseInternalWebClient(@Qualifier("internalWebClient") WebClient baseWebClient) {
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

    public <T> Mono<ResponseEntity<ApiResponse<T>>> get(String uri, Object parameters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(uri);

        // DTO 필드를 Map으로 변환 후 query param 추가
        BeanWrapper beanWrapper = new BeanWrapperImpl(parameters);
        for (var propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
            String name = propertyDescriptor.getName();
            Object value = beanWrapper.getPropertyValue(name);
            if (value != null) {
                builder.queryParam(name, value);
            }
        }

        return baseWebClient.get()
                .uri(builder.build().toUri())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<ApiResponse<T>>() {
                });
    }
}
