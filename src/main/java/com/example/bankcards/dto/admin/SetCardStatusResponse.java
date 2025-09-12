package com.example.bankcards.dto.admin;

import com.example.bankcards.entity.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Ответ после изменения статуса карты")
public record SetCardStatusResponse(

        @Schema(description = "ID карты", example = "123")
        Long cardId,

        @Schema(description = "Старый статус карты", example = "ACTIVE")
        CardStatus oldStatus,

        @Schema(description = "Новый статус карты", example = "BLOCKED")
        CardStatus newStatus
) {}
