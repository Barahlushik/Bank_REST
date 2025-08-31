package com.example.bankcards.dto.user;

public record CardBlockResponse(
        Long cardId,
        String status,
        String message
) {}