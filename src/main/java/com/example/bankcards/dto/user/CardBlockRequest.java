package com.example.bankcards.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


@Schema(description = "Запрос на блокировку карты")
public record CardBlockRequest(

        @NotBlank(message = "Reason required")
        @Schema(description = "Причина блокировки карты", example = "Карта утеряна")
        String reason
) {}