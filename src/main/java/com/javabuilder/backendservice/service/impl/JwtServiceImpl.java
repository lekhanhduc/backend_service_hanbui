package com.javabuilder.backendservice.service.impl;

import com.javabuilder.backendservice.exception.CustomException;
import com.javabuilder.backendservice.exception.ErrorCode;
import com.javabuilder.backendservice.service.JwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Override
    public String generateAccessToken(String userId) {
        // Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        // Payload
        Date now = new Date();
        Date expirationTime = Date.from(now.toInstant().plus(30, ChronoUnit.MINUTES));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issueTime(now)
                .expirationTime(expirationTime)
                .build();
        // Signature
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        try {
            signedJWT.sign(new MACSigner(secretKey));
            return signedJWT.serialize();
        } catch (JOSEException _) {
            throw new CustomException(ErrorCode.GENERATE_TOKEN_ERROR);
        }
    }

    @Override
    public String generateRefreshToken(String userId) {
        // Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        // Payload
        Date now = new Date();
        Date expirationTime = Date.from(now.toInstant().plus(30, ChronoUnit.DAYS));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issueTime(now)
                .expirationTime(expirationTime)
                .build();
        // Signature
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        try {
            signedJWT.sign(new MACSigner(secretKey));
            return signedJWT.serialize();
        } catch (JOSEException _) {
            throw new CustomException(ErrorCode.GENERATE_TOKEN_ERROR);
        }
    }

}
