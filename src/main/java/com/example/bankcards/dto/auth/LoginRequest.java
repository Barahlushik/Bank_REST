package com.example.bankcards.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Schema(description = "Запрос на аутентификацию с помощью логина и пароля")
public record LoginRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 30, message = "Username must be 4–30 characters")
        @Schema(
                description = "Уникальный логин пользователя",
                example = "barahlyush"
        )
        String username,

        @Size(min = 8, max = 32, message = "Пароль должен содержать от 8 до 32 символов")
        @NotBlank(message = "Пароль обязателен")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?^&])[A-Za-z\\d@$!%*#?^&]{8,32}$",
                message = "Пароль должен содержать как минимум одну букву, одну цифру и один спецсимвол"
        )
        @Schema(
                description = "Пароль для входа в систему",
                example = "P@ssw0rd2025!"
        )
        String password
) {}
