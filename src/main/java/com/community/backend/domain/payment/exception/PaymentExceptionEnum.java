package com.community.backend.domain.payment.exception;

import org.springframework.http.HttpStatus;

import com.community.backend.common.exception.ErrorEnum;

public enum PaymentExceptionEnum implements ErrorEnum {

	TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 토큰을 찾을 수 없습니다."),
	PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 검증에 실패하였습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	PaymentExceptionEnum(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
}
