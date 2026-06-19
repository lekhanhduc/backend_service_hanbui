package com.javabuilder.backendservice.controller;

import com.javabuilder.backendservice.dto.request.AuthenticationRequest;
import com.javabuilder.backendservice.dto.response.ApiResponse;
import com.javabuilder.backendservice.dto.response.AuthenticationResponse;
import com.javabuilder.backendservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request, HttpServletResponse response) {
        var data = authenticationService.authenticate(request);
        var refreshToken = data.getRefreshToken();

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false)
                .domain("localhost")
                .path("/api/v1/auth")
                .maxAge(Duration.ofDays(14))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        data.setRefreshToken(null);

        return ApiResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Login successful")
                .data(data)
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        var data = authenticationService.refreshToken(refreshToken);
        return ApiResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Token refreshed successfully")
                .data(data)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@CookieValue("refresh_token") String refreshToken) {
        authenticationService.logout(refreshToken);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Logout successfully")
                .build();
    }
}