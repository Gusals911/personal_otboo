package com.gusals911.otboo.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 서비스 예외 처리
    @ExceptionHandler(OtbooException.class)
    public ResponseEntity<ErrorResponse> handleOtbooException(OtbooException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ErrorResponse response = new ErrorResponse(
                exception.getClass().getSimpleName(),
                errorCode.name(),
                errorCode.getMessage(),
                exception.getDetails()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }
    // DTO 객체 내부 필드 검증 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        ErrorCode errorCode = ErrorCode.COMMON_INVALID_INPUT;

        Map<String, Object> details = new LinkedHashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage() == null
                    ? "올바르지 않은 값입니다."
                    : fieldError.getDefaultMessage();

            details.putIfAbsent(field, message);
        }

        ErrorResponse response = new ErrorResponse(
                exception.getClass().getSimpleName(),
                errorCode.name(),
                errorCode.getMessage(),
                details
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }
    // 컨트롤러 메서드 개별 파라미터 검증 실패 예외 처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        ErrorCode errorCode = ErrorCode.COMMON_INVALID_INPUT;

        Map<String, Object> details = new LinkedHashMap<>();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();

            details.putIfAbsent(field, message);
        }

        ErrorResponse response = new ErrorResponse(
                exception.getClass().getSimpleName(),
                errorCode.name(),
                errorCode.getMessage(),
                details
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }
    // 타입 변환 실패 예외 처리
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception
    ) {
        ErrorCode errorCode = ErrorCode.COMMON_INVALID_TYPE;

        Map<String, Object> details = new LinkedHashMap<>();

        details.put("name", exception.getName());
        details.put("value", exception.getValue());
        details.put("requiredType", exception.getRequiredType() == null
                ? null
                : exception.getRequiredType().getSimpleName());

        ErrorResponse response = new ErrorResponse(
                exception.getClass().getSimpleName(),
                errorCode.name(),
                errorCode.getMessage(),
                details
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }
    // Body 읽기 실패, DTO 변환 실패 예외 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        ErrorCode errorCode = ErrorCode.COMMON_INVALID_REQUEST;

        ErrorResponse response = new ErrorResponse(
                exception.getClass().getSimpleName(),
                errorCode.name(),
                errorCode.getMessage(),
                Map.of()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }
    // 이외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorCode errorCode = ErrorCode.COMMON_INTERNAL_SERVER_ERROR;

        ErrorResponse response = new ErrorResponse(
                exception.getClass().getSimpleName(),
                errorCode.name(),
                errorCode.getMessage(),
                Map.of()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }
}
