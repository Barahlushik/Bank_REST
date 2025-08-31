package com.example.bankcards.dto.auth;

public record RegisterResponse(
        Long userId,
        String username,
        String email
) {}
