package com.example.bankcards.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferBetweenMyCardsRequest(
        @NotNull @Min(1)
        Long fromCardId,

        @NotNull @Min(1)
        Long toCardId,

        @NotNull
        @Min(1)
        BigDecimal amount
) {}
