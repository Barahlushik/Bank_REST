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
        Long id,

        @Schema(
                description = "Флаг успешности операции",
                example = "true"
        )
        boolean successful,

        @Schema(
                description = "Текущий статус карты (например, ACTIVE, BLOCKED, EXPIRED)",
                example = "ACTIVE",
                implementation = CardStatus.class
        )
        CardStatus status


) {}
