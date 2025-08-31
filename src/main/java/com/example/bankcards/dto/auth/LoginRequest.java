package com.example.bankcards.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Username required")
        String username,

        @NotBlank(message = "Password required")
        String password
) {}
