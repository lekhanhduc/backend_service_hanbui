package com.javabuilder.backendservice.service;

import com.javabuilder.backendservice.dto.internal.JwtDetails;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.List;

public interface JwtService {
    JwtDetails generateAccessToken(String userId, List<String> authorities);
    JwtDetails generateRefreshToken(String userId);
    SignedJWT verifyRefreshToken(String token) throws ParseException, JOSEException;
}
