package com.community.backend.common.webclient.payment;

import org.springframework.http.HttpStatus;

import com.community.backend.common.exception.ErrorEnum;

public enum PaymentInternalException implements ErrorEnum {

	CHARGE_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 기록이 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	PaymentInternalException(HttpStatus httpStatus, String message) {
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