package com.javabuilder.backendservice.service;

public interface JwtService {
    String generateAccessToken(String userId);
    String generateRefreshToken(String userId);
}
