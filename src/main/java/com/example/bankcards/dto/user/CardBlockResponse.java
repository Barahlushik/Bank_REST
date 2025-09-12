package com.example.bankcards.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ после запроса на блокировку карты")
public record CardBlockResponse(

        @Schema(description = "ID карты", example = "101")
        Long cardId,

        @Schema(description = "Сообщение о результате операции", example = "Запрос на блокировку успешно принят")
        String message
) {}