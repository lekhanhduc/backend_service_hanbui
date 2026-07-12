package com.javabuilder.backendservice.dto.response;

import lombok.Builder;

@Builder
public record PreSignedResponse(
        String preSignedUrl,
        String objectKey
) {
}
