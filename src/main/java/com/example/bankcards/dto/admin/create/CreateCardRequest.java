package com.example.bankcards.dto.admin.create;

import com.example.bankcards.entity.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Запрос на создание новой банковской карты")
public record CreateCardRequest(

        @NotNull(message = "Card type is required")
        @Schema(
                description = "Тип карты",
                example = "CREDIT",
                implementation = CardType.class
        )
        CardType type,


        @NotNull(message = "ownerId is required")
        @Schema(
                description = "Идентификатор владельца карты (пользователь в системе).",
                example = "1001"
        )
        Long ownerId

) {}