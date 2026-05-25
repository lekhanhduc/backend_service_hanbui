package com.javabuilder.backendservice.service;

import com.javabuilder.backendservice.dto.request.CreateUserRequest;
import com.javabuilder.backendservice.dto.request.UpdateUserRequest;
import com.javabuilder.backendservice.dto.response.CreateUserResponse;
import com.javabuilder.backendservice.dto.response.UserDetailResponse;
import java.util.List;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest request);
    UserDetailResponse getUserDetailById(String id);
    UserDetailResponse updateUserById(String id, UpdateUserRequest request);
    void deleteUserById(String id);
    List<UserDetailResponse> getAllUsers();
}
