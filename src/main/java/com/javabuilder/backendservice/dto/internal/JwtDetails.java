package com.javabuilder.backendservice.dto.internal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtDetails {
    private String value;
    private String jwtId;
    private long expiryTime;
    private long secondsTtl;
}
