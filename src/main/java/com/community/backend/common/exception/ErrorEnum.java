package com.community.backend.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorEnum {

	String getMessage();

	HttpStatus getHttpStatus();
}
