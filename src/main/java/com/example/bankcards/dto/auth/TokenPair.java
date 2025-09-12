package com.example.bankcards.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Пара токенов для авторизации: Access + Refresh")
public record TokenPair(

        @Schema(
                description = "JWT Access token. Используется для авторизации запросов в течение короткого времени."
        )
        String accessToken,

        @Schema(
                description = "JWT Refresh token. Используется для получения нового Access token. " +
                        "Срок жизни обычно дольше (например, 7 дней). " +
                        "Должен храниться только на клиенте и не передаваться в запросах, кроме /refresh."
        )
        String refreshToken
) {}
