package com.community.backend.domain.member.exception;

import org.springframework.http.HttpStatus;

import com.community.backend.common.exception.ErrorEnum;


public enum MemberExceptionEnum implements ErrorEnum {

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
	DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
	DUPLICATE_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "잘못된 로그인 타입입니다."),
	MEMBER_INACTIVE(HttpStatus.FORBIDDEN, "비활성화된 사용자입니다.");

	private final HttpStatus httpStatus;
	private final String message;

	MemberExceptionEnum(HttpStatus httpStatus, String message) {
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
