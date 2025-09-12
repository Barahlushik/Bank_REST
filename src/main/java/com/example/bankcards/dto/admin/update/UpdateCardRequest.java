package com.example.bankcards.dto.admin.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.math.BigDecimal;
import java.time.LocalDate;



@Schema(description = "Запрос на обновление существующей карты")
public record UpdateCardRequest(

        @NotNull(message = "Card ID required")
        @Schema(description = "ID обновляемой карты", example = "1001")
        Long cardId,

        @Size(min = 12, max = 19, message = "Card number must be between 12 and 19 digits")
        @Schema(description = "Новый номер карты (12–19 цифр)", example = "5555666677778888")
        String number,

        @Future(message = "Expiration must be in the future")
        @Schema(description = "Новая дата истечения срока действия карты",
                example = "2031-05-20", type = "string", format = "date")
        LocalDate expiration,

        @DecimalMin(value = "0.00", message = "Balance cannot be negative")
        @Schema(description = "Обновленный баланс карты", example = "2000.00")
        BigDecimal balance
) {}