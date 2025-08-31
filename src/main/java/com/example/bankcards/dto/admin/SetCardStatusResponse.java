package com.example.bankcards.dto.admin;

import com.example.bankcards.entity.enums.CardStatus;

public record SetCardStatusResponse(
        Long cardId,
        CardStatus oldStatus,
        CardStatus newStatus
) {}