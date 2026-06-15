package com.javabuilder.backendservice.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.List;

public interface JwtService {
    String generateAccessToken(String userId, List<String> authorities);
    String generateRefreshToken(String userId);
    SignedJWT verifyRefreshToken(String token) throws ParseException, JOSEException;
}
