package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Ответ с балансом карты")
public record BalanceResponse(

        @Schema(description = "ID карты", example = "101")
        Long cardId,

        @Schema(description = "Текущий баланс карты", example = "1250.75")
        BigDecimal balance
) {}