package com.javabuilder.backendservice.service.impl;

import com.javabuilder.backendservice.dto.request.CreateUserRequest;
import com.javabuilder.backendservice.dto.request.UpdateUserRequest;
import com.javabuilder.backendservice.dto.response.CreateUserResponse;
import com.javabuilder.backendservice.dto.response.UserDetailResponse;
import com.javabuilder.backendservice.entity.User;
import com.javabuilder.backendservice.repository.UserRepository;
import com.javabuilder.backendservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {
        if(userRepository.existsByEmail(request.email()))
            throw new RuntimeException("User already exists");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .displayName(request.displayName())
                .build();

        userRepository.save(user);
        return CreateUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .build();
    }

    @Override
    public UserDetailResponse getUserDetailById(String id) {
        return userRepository.findById(id)
                .map(user -> UserDetailResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .displayName(user.getDisplayName())
                        .build())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDetailResponse updateUserById(String id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setDisplayName(request.displayName());
        userRepository.save(user);
        return UserDetailResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .build();
    }

    @Override
    public void deleteUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDetailResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDetailResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .displayName(user.getDisplayName())
                        .build())
                .toList();
    }
}
