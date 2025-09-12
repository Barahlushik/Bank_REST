package com.example.bankcards.dto.admin;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Фильтр для поиска карт (применяется администратором)")
public record CardFilter(

        @Schema(
                description = "Фильтр по статусу карты (ACTIVE, BLOCKED, EXPIRED)",
                example = "ACTIVE"
        )
        CardStatus status,

        @Schema(
                description = "Фильтр по типу карты (DEBIT, CREDIT, VIRTUAL)",
                example = "DEBIT"
        )
        CardType type,

        @Schema(
                description = "Найти карты с датой истечения раньше указанной",
                example = "2027-01-01",
                type = "string",
                format = "date"
        )
        LocalDate expirationBefore,

        @Schema(
                description = "Найти карты с датой истечения позже указанной",
                example = "2024-12-31",
                type = "string",
                format = "date"
        )
        LocalDate expirationAfter
) {}