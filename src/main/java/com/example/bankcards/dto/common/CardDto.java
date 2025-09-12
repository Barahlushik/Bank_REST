package com.example.bankcards.dto.common;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;


@Schema(description = "Банковская карта")
public record CardDto(

        @Schema(description = "Уникальный идентификатор карты", example = "1001")
        Long id,

        @Schema(description = "Тип карты", example = "CREDIT", implementation = CardType.class)
        CardType type,

        @Schema(description = "Номер карты (может быть замаскирован)", example = "411111******1111")
        String number,

        @Schema(description = "Дата истечения срока действия карты",
                example = "2030-12-31",
                type = "string",
                format = "date")
        LocalDate expiration,

        @Schema(description = "Текущий статус карты (ACTIVE, BLOCKED, EXPIRED)",
                example = "ACTIVE",
                implementation = CardStatus.class)
        CardStatus status,

        @Schema(description = "Баланс карты", example = "2500.75")
        BigDecimal balance
) {}