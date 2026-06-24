package com.javabuilder.backendservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "RedisToken")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    private String jwtId;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private long secondsTtl;
}
