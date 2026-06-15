package com.javabuilder.backendservice.service.impl;

import com.javabuilder.backendservice.common.TokenType;
import com.javabuilder.backendservice.exception.CustomException;
import com.javabuilder.backendservice.exception.ErrorCode;
import com.javabuilder.backendservice.service.JwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Override
    public String generateAccessToken(String userId, List<String> authorities) {
        // Header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        // Payload
        Date now = new Date();
        Date expirationTime = Date.from(now.toInstant().plus(30, ChronoUnit.MINUTES));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issuer(issuer)
                .issueTime(now)
                .expirationTime(expirationTime)
                .claim("typ", TokenType.ACCESS.name())
                .claim("authorities", authorities)
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
                .issuer(issuer)
                .issueTime(now)
                .expirationTime(expirationTime)
                .claim("typ", TokenType.REFRESH.name())
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
    public SignedJWT verifyRefreshToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean isValid = signedJWT.verify(new MACVerifier(secretKey));
        if(!isValid) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        TokenType tokenType = TokenType.valueOf(signedJWT.getJWTClaimsSet().getStringClaim("typ"));
        if(TokenType.REFRESH != tokenType) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if(expirationTime.before(new Date())) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        return signedJWT;
    }

}
