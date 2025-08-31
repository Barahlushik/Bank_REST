package com.example.bankcards.dto.admin.update;

import com.example.bankcards.entity.enums.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateCardResponse(
        Long cardId,
        String number,
        LocalDate expiration,
        CardStatus status,
        BigDecimal balance
) {}
