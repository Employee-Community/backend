package com.community.backend.common.exception;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.community.backend.common.dto.ApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	/**
	* Custom 예외처리
	*/
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ApiResponse<Void>> handlerCustomException(BaseException ex) {

		return ResponseEntity
			.status(ex.getErrorEnum().getHttpStatus())
			.body(ApiResponse.fail(ex.getErrorEnum().getMessage(), null));
	}

	/**
	 * @RequestBody JSON TO Dto Valid 예외처리
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

		return handlerBindingResult(ex.getBindingResult());
	}

	/**
	 * Param / ModelAttribute binding Valid 예외처리
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ApiResponse<Void>> handlerBindException(BindException ex) {

		return handlerBindingResult(ex.getBindingResult());
	}

	/**
	 * Param / Path 단일 binding Validated exception 예외처리
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handlerConstraintViolationException(ConstraintViolationException ex) {

		String msg = ex.getConstraintViolations()
			.stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.joining(","));

		return ResponseEntity
			.badRequest()
			.body(ApiResponse.fail(msg, null));
	}

	/**
	 * Type mismatch 예외처리
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiResponse<Void>> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {

		String msg = String.format("잘못된 요청입니다. %s : %s", ex.getName(), ex.getValue());

		return ResponseEntity
			.badRequest()
			.body(ApiResponse.fail(msg, null));
	}

	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ApiResponse.fail("예상치 못한 오류가 발생했습니다.", null));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ApiResponse<Void>> handleException(Exception e) {

		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ApiResponse.fail("서버 내부 오류가 발생했습니다.", null));
	}

	private ResponseEntity<ApiResponse<Void>> handlerBindingResult(BindingResult bindingResult) {

		String msg = bindingResult.getFieldErrors()
			.stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.joining(", "));

		return ResponseEntity.badRequest().body(ApiResponse.fail(msg, null));
	}
}
