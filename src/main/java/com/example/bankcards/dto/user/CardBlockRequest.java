package com.example.bankcards.dto.user;

import jakarta.validation.constraints.NotBlank;

public record CardBlockRequest(
        @NotBlank(message = "Reason required")
        String reason
) {}

