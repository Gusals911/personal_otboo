package com.gusals911.otboo.global.exception;

import java.util.Map;

public record ErrorResponse(
        String errorName,
        String code,
        String message,
        Map<String, Object> details
) {
}
