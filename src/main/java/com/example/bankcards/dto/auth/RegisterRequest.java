package com.example.bankcards.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username required")
        @Size(min = 4, max = 30)
        String username,

        @NotBlank(message = "Password required")
        @Size(min = 6, max = 100)
        String password,

        @Email
        @NotBlank(message = "Email required")
        String email
) {}