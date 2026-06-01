package com.javabuilder.backendservice.service;

import com.javabuilder.backendservice.dto.request.CreateUserRequest;
import com.javabuilder.backendservice.dto.request.UpdateUserRequest;
import com.javabuilder.backendservice.dto.response.CreateUserResponse;
import com.javabuilder.backendservice.dto.response.PageResponse;
import com.javabuilder.backendservice.dto.response.UserDetailResponse;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest request);
    UserDetailResponse getUserDetailById(String id);
    UserDetailResponse updateUserById(String id, UpdateUserRequest request);
    void deleteUserById(String id);
    PageResponse<UserDetailResponse> getAllUsers(int page, int size, String email, String displayName);
    UserDetailResponse getEmailDisplayName(String email);
}
