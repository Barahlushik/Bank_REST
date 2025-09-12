package com.example.bankcards.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ после успешной регистрации пользователя")
public record RegisterResponse(

        @Schema(
                description = "JWT Access token. Используется в заголовке Authorization: Bearer <token> " +
                        "для доступа к защищённым ресурсам. Обычно живёт 10–30 минут.",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        String accessToken
) {}

