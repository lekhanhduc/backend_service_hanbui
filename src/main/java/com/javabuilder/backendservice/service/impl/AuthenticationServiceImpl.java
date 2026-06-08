package com.javabuilder.backendservice.service.impl;

import com.javabuilder.backendservice.dto.request.AuthenticationRequest;
import com.javabuilder.backendservice.dto.response.AuthenticationResponse;
import com.javabuilder.backendservice.entity.User;
import com.javabuilder.backendservice.service.AuthenticationService;
import com.javabuilder.backendservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authenticate = authenticationManager.authenticate(authenticationToken);
        var user = (User) authenticate.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        return AuthenticationResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
