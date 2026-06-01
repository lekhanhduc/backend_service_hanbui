package com.javabuilder.backendservice.controller;

import com.javabuilder.backendservice.dto.request.CreateUserRequest;
import com.javabuilder.backendservice.dto.request.UpdateUserRequest;
import com.javabuilder.backendservice.dto.response.ApiResponse;
import com.javabuilder.backendservice.dto.response.CreateUserResponse;
import com.javabuilder.backendservice.dto.response.PageResponse;
import com.javabuilder.backendservice.dto.response.UserDetailResponse;
import com.javabuilder.backendservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        var data = userService.createUser(request);
        return ApiResponse.<CreateUserResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("User created successfully")
                .data(data)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDetailResponse> getUserDetailById(@PathVariable String id) {
        var data = userService.getUserDetailById(id);
        return ApiResponse.<UserDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("User retrieved successfully")
                .data(data)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDetailResponse> updateUserById(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        var data = userService.updateUserById(id, request);
        return ApiResponse.<UserDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("User updated successfully")
                .data(data)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUserById(@PathVariable String id) {
        userService.deleteUserById(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("User deleted successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<UserDetailResponse>> getAllUsers(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String email,
            @RequestParam(required = false, defaultValue = "") String displayName) {

        var data = userService.getAllUsers(page, size, email, displayName);
        return ApiResponse.<PageResponse<UserDetailResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Users retrieved successfully")
                .data(data)
                .build();
    }

    @GetMapping("/email")
    public ApiResponse<UserDetailResponse> getEmailDisplayName(@RequestParam String email) {
        var data = userService.getEmailDisplayName(email);
        return ApiResponse.<UserDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("User retrieved successfully")
                .data(data)
                .build();
    }

}
