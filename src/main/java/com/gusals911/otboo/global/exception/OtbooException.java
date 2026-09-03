package com.gusals911.otboo.global.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OtbooException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    protected OtbooException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = Map.of();
    }

    protected OtbooException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details == null ? Map.of() : Map.copyOf(details);
    }
}