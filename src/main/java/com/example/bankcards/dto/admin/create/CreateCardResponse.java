package com.example.bankcards.dto.admin.create;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Schema(description = "Ответ после успешного создания банковской карты")
public record CreateCardResponse(

        @Schema(
                description = "Уникальный идентификатор созданной карты",
                example = "5001"
        )
        Long cardId,

        @Schema(
                description = "Тип карты",
                example = "DEBIT",
                implementation = CardType.class
        )
        CardType type,

        @Schema(
                description = "Номер карты (может быть замаскирован при возврате)",
                example = "411111******1111"
        )
        String number,

        @Schema(
                description = "Дата истечения срока действия карты",
                example = "2030-12-31",
                type = "string",
                format = "date"
        )
        LocalDate expiration,

        @Schema(
                description = "Текущий статус карты (например, ACTIVE, BLOCKED, EXPIRED)",
                example = "ACTIVE",
                implementation = CardStatus.class
        )
        CardStatus status,

        @Schema(
                description = "Текущий баланс карты",
                example = "1500.50"
        )
        BigDecimal balance
) {}
