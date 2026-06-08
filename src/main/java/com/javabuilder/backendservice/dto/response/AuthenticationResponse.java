package com.javabuilder.backendservice.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String userId,
        String accessToken,
        String refreshToken
) { }
