package com.example.bankcards.dto.admin.create;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCardResponse(
        Long cardId,
        CardType type,
        String number,
        LocalDate expiration,
        CardStatus status,
        BigDecimal balance
) {}
