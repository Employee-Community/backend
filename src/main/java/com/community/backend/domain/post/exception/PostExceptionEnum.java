package com.community.backend.domain.post.exception;

import org.springframework.http.HttpStatus;

import com.community.backend.common.exception.ErrorEnum;

public enum PostExceptionEnum implements ErrorEnum {

	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 글을 찾을 수 없습니다."),
	POST_MEMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "작성자만 수정 / 삭제할 수 있습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	PostExceptionEnum(HttpStatus httpStatus, String message) {
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
