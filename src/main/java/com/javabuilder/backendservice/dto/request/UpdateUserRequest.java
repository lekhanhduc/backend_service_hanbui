package com.javabuilder.backendservice.dto.request;

public record UpdateUserRequest(
        String email,
        String displayName
) {
}
