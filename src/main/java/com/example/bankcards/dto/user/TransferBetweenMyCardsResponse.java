package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;


@Schema(description = "Ответ после перевода между картами пользователя")
public record TransferBetweenMyCardsResponse(

        @Schema(description = "ID карты-отправителя", example = "101")
        Long fromCardId,

        @Schema(description = "ID карты-получателя", example = "102")
        Long toCardId,

        @Schema(description = "Сумма перевода", example = "500.00")
        BigDecimal amount,

        @Schema(description = "Статус перевода", example = "SUCCESS")
        String status
) {}