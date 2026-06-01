package com.javabuilder.backendservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handlerCustomException(CustomException exception, WebRequest request) {
        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getCode())
                .message(errorCode.getMessage())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

}
