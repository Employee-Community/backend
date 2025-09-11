package com.community.backend.common.kafka.dto;

public record MessageWrapper<T>(
	T data,
	String msgName
) {

	public static <T> MessageWrapper<T> create(T data, String msgName) {

		return new MessageWrapper<>(data, msgName);
	}
}
