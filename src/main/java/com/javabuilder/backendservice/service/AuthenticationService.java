package com.javabuilder.backendservice.service;

import com.javabuilder.backendservice.dto.request.AuthenticationRequest;
import com.javabuilder.backendservice.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
