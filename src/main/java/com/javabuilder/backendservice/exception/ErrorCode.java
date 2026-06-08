package com.javabuilder.backendservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    USER_EXISTED(400, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),

    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    GENERATE_TOKEN_ERROR(500, "Error generating token", HttpStatus.INTERNAL_SERVER_ERROR)

    ;
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
