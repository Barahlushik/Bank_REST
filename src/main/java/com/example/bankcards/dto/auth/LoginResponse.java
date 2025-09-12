package com.example.bankcards.dto.auth;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ после успешной аутентификации (только Access token)")
public record LoginResponse(

        @Schema(
                description = "JWT Access token. Используется в заголовке Authorization: Bearer <token> " +
                        "для доступа к защищённым ресурсам. Срок жизни обычно короткий (например, 15 минут).",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        String accessToken
) {}