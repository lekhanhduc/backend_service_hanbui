package com.javabuilder.backendservice.service.impl;

import com.javabuilder.backendservice.dto.request.AuthenticationRequest;
import com.javabuilder.backendservice.dto.response.AuthenticationResponse;
import com.javabuilder.backendservice.entity.User;
import com.javabuilder.backendservice.exception.CustomException;
import com.javabuilder.backendservice.exception.ErrorCode;
import com.javabuilder.backendservice.repository.UserRepository;
import com.javabuilder.backendservice.service.AuthenticationService;
import com.javabuilder.backendservice.service.JwtService;
import com.javabuilder.backendservice.utils.SecurityUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.text.ParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authenticate = authenticationManager.authenticate(authenticationToken);
        var user = (User) authenticate.getPrincipal();

        List<String> authorities = SecurityUtils.getAuthorities(user);

        String accessToken = jwtService.generateAccessToken(user.getId(), authorities);
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        return AuthenticationResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        if(!StringUtils.hasText(refreshToken)) {
            throw new CustomException(ErrorCode.COOKIE_IS_REQUIRE);
        }
        try {
            SignedJWT signedJWT = jwtService.verifyRefreshToken(refreshToken);
            String userId = signedJWT.getJWTClaimsSet().getSubject();
            User user = userRepository.findByIdWithRoles(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            List<String> authorities = SecurityUtils.getAuthorities(user);

            String accessToken = jwtService.generateAccessToken(userId, authorities);

            return AuthenticationResponse.builder()
                    .userId(userId)
                    .accessToken(accessToken)
                    .build();
        } catch (ParseException | JOSEException _) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
}
