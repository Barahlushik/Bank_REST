package com.example.bankcards.dto.admin.create;

import com.example.bankcards.entity.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Запрос на создание новой банковской карты")
public record CreateCardRequest(

        @NotNull(message = "Card type is required")
        @Schema(
                description = "Тип карты",
                example = "CREDIT",
                implementation = CardType.class
        )
        CardType type,

        @NotBlank(message = "Card number is required")
        @Size(min = 12, max = 19, message = "Card number must be between 12 and 19 digits")
        @Schema(
                description = "Номер карты (12–19 цифр). Должен быть уникален в системе.",
                example = "2282282282282288"
        )
        String number,

        @NotNull(message = "Expiration date is required")
        @Future(message = "Expiration must be in the future")
        @Schema(
                description = "Дата истечения срока действия карты (должна быть будущей датой).",
                example = "2030-12-31",
                type = "string",
                format = "date"
        )
        LocalDate expiration,

        @NotNull(message = "ownerId is required")
        @Schema(
                description = "Идентификатор владельца карты (пользователь в системе).",
                example = "1001"
        )
        Long ownerId,

        @NotNull(message = "Initial balance required")
        @DecimalMin(value = "0.00", message = "Balance cannot be negative")
        @Schema(
                description = "Начальный баланс карты. Не может быть отрицательным.",
                example = "1500.50"
        )
        BigDecimal balance
) {}