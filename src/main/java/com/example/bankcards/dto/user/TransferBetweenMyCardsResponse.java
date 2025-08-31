package com.example.bankcards.dto.user;

import java.math.BigDecimal;

public record TransferBetweenMyCardsResponse(
        Long fromCardId,
        Long toCardId,
        BigDecimal amount,
        String status
) {}
