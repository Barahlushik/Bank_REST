package com.example.bankcards.dto.admin.update;

import com.example.bankcards.entity.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Ответ после обновления карты")
public record UpdateCardResponse(

        @Schema(description = "ID карты", example = "1001")
        Long cardId,

        @Schema(description = "Номер карты (может быть замаскирован)", example = "555566******8888")
        String number,

        @Schema(description = "Дата истечения срока действия", example = "2031-05-20",
                type = "string", format = "date")
        LocalDate expiration,

        @Schema(description = "Текущий статус карты", example = "ACTIVE")
        CardStatus status,

        @Schema(description = "Текущий баланс карты", example = "2000.00")
        BigDecimal balance
) {}
