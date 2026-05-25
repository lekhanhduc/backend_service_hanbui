package com.javabuilder.backendservice.dto.request;

public record CreateUserRequest(
        String email,
        String password,
        String displayName
) {
}
