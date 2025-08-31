package com.example.bankcards.dto.admin.create;

import com.example.bankcards.entity.enums.CardType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCardRequest(
        @NotNull(message = "Card type is required")
        CardType type,

        @NotBlank(message = "Card number is required")
        @Size(min = 12, max = 19, message = "Card number must be between 12 and 19 digits")
        String number,

        @NotNull(message = "Expiration date is required")
        @Future(message = "Expiration must be in the future")
        LocalDate expiration,

        @NotNull(message = "Initial balance required")
        @DecimalMin(value = "0.00", message = "Balance cannot be negative")
        BigDecimal balance
) {}