package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Запрос на перевод средств между картами одного пользователя")
public record TransferBetweenMyCardsRequest(

        @NotNull @Min(1)
        @Schema(description = "ID карты-отправителя", example = "101")
        Long fromCardId,

        @NotNull @Min(1)
        @Schema(description = "ID карты-получателя", example = "102")
        Long toCardId,

        @NotNull
        @DecimalMin(value = "0.00", message = "Amount must be positive")
        @Schema(description = "Сумма перевода", example = "500.00")
        BigDecimal amount
) {}