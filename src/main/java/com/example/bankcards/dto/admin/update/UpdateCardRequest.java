package com.example.bankcards.dto.admin.update;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateCardRequest(
        @NotNull(message = "Card ID required")
        Long cardId,

        @Size(min = 12, max = 19, message = "Card number must be between 12 and 19 digits")
        String number,

        @Future(message = "Expiration must be in the future")
        LocalDate expiration,

        @DecimalMin(value = "0.00", message = "Balance cannot be negative")
        BigDecimal balance
) {}
