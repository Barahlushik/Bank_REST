package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "")
        String username,

        @NotBlank(message = "")
        String password
) {}
