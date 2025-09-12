package com.example.bankcards.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Запрос на регистрацию нового пользователя")
public record RegisterRequest(

        @NotBlank(message = "Username required")
        @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters")
        @Schema(
                description = "Уникальный логин пользователя. Должен содержать от 4 до 30 символов.",
                example = "barahlyush"
        )
        String username,

        @NotBlank(message = "Password required")
        @Size(min = 6, max = 72, message = "Password must be 6–72 characters")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?^&])[A-Za-z\\d@$!%*#?^&]{8,32}$",
                message = "Пароль должен содержать как минимум одну букву, одну цифру и один спецсимвол"
        )
        @Schema(
                description = "Пароль пользователя. Минимум 6, максимум 72 символа. " +
                        "В продакшн-регистрации часто применяют правила сложности (буквы, цифры, спецсимволы).",
                example = "Str0ngP@ssw0rd!"
        )
        String password,

        @Email(message = "Must be a valid email address")
        @NotBlank(message = "Email required")
        @Schema(
                description = "Email пользователя. Используется для подтверждения и восстановления доступа.",
                example = "user@example.com"
        )
        String email,

        @NotBlank(message = "Name required")
        @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
        @Schema(
                description = "Имя пользователя (реальное или отображаемое).",
                example = "Иван"
        )
        String name,

        @NotBlank(message = "Surname required")
        @Size(min = 1, max = 50, message = "Surname must be between 1 and 50 characters")
        @Schema(
                description = "Фамилия пользователя.",
                example = "Иванов"
        )
        String surname,

        @NotNull(message = "Birth date required")
        @Past(message = "Birth date must be in the past")
        @Schema(
                description = "Дата рождения (должна быть в прошлом). Может использоваться для проверки возраста.",
                example = "1995-08-15",
                type = "string",
                format = "date"
        )
        LocalDate birthDate
) {}
