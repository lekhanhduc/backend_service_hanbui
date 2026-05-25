package com.javabuilder.backendservice.controller;

import com.javabuilder.backendservice.dto.request.CreateUserRequest;
import com.javabuilder.backendservice.dto.request.UpdateUserRequest;
import com.javabuilder.backendservice.dto.response.CreateUserResponse;
import com.javabuilder.backendservice.dto.response.UserDetailResponse;
import com.javabuilder.backendservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/users/{id}")
    public UserDetailResponse getUserDetailById(@PathVariable String id) {
        return userService.getUserDetailById(id);
    }

    @PutMapping("/users/{id}")
    public UserDetailResponse updateUserById(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        return userService.updateUserById(id, request);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable String id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/users")
    public List<UserDetailResponse> getAllUsers() {
        return userService.getAllUsers();
    }
}
