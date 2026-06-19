package com.javabuilder.backendservice.security;

import com.javabuilder.backendservice.common.TokenType;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomOAuth2TokenValidator implements OAuth2TokenValidator<Jwt> {

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        TokenType typ = TokenType.valueOf(jwt.getClaimAsString("typ"));
        if(typ != TokenType.ACCESS) {
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("401", "typ claim cannot be null", null));
        }
        return OAuth2TokenValidatorResult.success();
    }
}
