package com.example.bankcards.dto.user;

import java.math.BigDecimal;

public record BalanceResponse(
        Long cardId,
        BigDecimal balance
) {}
