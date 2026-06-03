package com.javabuilder.backendservice.dto.request;

import com.javabuilder.backendservice.validator.DateOfBirth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank
        @Email(message = "Email is invalid")
        String email,

        @NotBlank(message = "Password is require")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotBlank(message = "Display name is require")
        String displayName,

        @DateOfBirth(message = "Date of birth must be greater than or equal 16 years old")
        LocalDate birthDate
) { }
