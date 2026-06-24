package com.javabuilder.backendservice.service.impl;

import com.javabuilder.backendservice.dto.internal.JwtDetails;
import com.javabuilder.backendservice.dto.request.AuthenticationRequest;
import com.javabuilder.backendservice.dto.response.AuthenticationResponse;
import com.javabuilder.backendservice.entity.Token;
import com.javabuilder.backendservice.entity.User;
import com.javabuilder.backendservice.exception.CustomException;
import com.javabuilder.backendservice.exception.ErrorCode;
import com.javabuilder.backendservice.repository.TokenRepository;
import com.javabuilder.backendservice.repository.UserRepository;
import com.javabuilder.backendservice.service.AuthenticationService;
import com.javabuilder.backendservice.service.JwtService;
import com.javabuilder.backendservice.utils.SecurityUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.text.ParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authenticate = authenticationManager.authenticate(authenticationToken);
        var user = (User) authenticate.getPrincipal();

        List<String> authorities = SecurityUtils.getAuthorities(user);

        JwtDetails accessDetails = jwtService.generateAccessToken(user.getId(), authorities);
        JwtDetails refreshDetails = jwtService.generateRefreshToken(user.getId());

        Token token = Token.builder()
                .jwtId(refreshDetails.getJwtId())
                .secondsTtl((refreshDetails.getSecondsTtl()))
                .build();
        tokenRepository.save(token);

        return AuthenticationResponse.builder()
                .userId(user.getId())
                .accessToken(accessDetails.getValue())
                .refreshToken(refreshDetails.getValue())
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        if(!StringUtils.hasText(refreshToken)) {
            throw new CustomException(ErrorCode.COOKIE_IS_REQUIRE);
        }
        try {
            SignedJWT signedJWT = jwtService.verifyRefreshToken(refreshToken);
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            if(!tokenRepository.existsById(jwtId)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }
            String userId = signedJWT.getJWTClaimsSet().getSubject();
            User user = userRepository.findByIdWithRoles(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            List<String> authorities = SecurityUtils.getAuthorities(user);
            JwtDetails accessDetails = jwtService.generateAccessToken(userId, authorities);

            return AuthenticationResponse.builder()
                    .userId(userId)
                    .accessToken(accessDetails.getValue())
                    .build();
        } catch (ParseException | JOSEException _) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public void logout(String refreshToken){
        try {
            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            tokenRepository.deleteById(jwtId);
        }catch (ParseException exception) {
            log.error("Failed to parse refresh token: {}", refreshToken, exception);
        }
    }
}
