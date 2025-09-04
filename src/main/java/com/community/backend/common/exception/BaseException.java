package com.community.backend.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

	private final ErrorEnum errorEnum;

	public BaseException(ErrorEnum errorEnum) {
		super(errorEnum.getMessage());
		this.errorEnum = errorEnum;
	}
}
