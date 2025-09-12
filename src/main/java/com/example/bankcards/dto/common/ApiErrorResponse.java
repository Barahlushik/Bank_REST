package com.example.bankcards.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Стандартный ответ об ошибке")
public record ApiErrorResponse(

        @Schema(description = "Краткое название ошибки", example = "Bad Request")
        String error,

        @Schema(description = "Подробное сообщение об ошибке", example = "Card with id 999 not found")
        String message,

        @Schema(description = "HTTP статус-код", example = "404")
        int status,

        @Schema(description = "Время возникновения ошибки",
                example = "2025-09-12T14:23:45",
                type = "string",
                format = "date-time")
        LocalDateTime timestamp
) {}