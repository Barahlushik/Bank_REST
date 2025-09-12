package com.example.bankcards.controller;

import com.example.bankcards.dto.auth.*;
import com.example.bankcards.dto.common.ApiErrorResponse;
import com.example.bankcards.service.api.auth.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

import static com.example.bankcards.util.SecurityConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Регистрация, вход и выход пользователей")
public class SecurityController {

    private final Auth auth;

    @Operation(
            summary = "Регистрация нового пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная регистрация",
                            content = @Content(schema = @Schema(implementation = RegisterResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные запроса",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Пользователь с таким логином или email уже существует",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody(
                    description = "Данные для регистрации нового пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody RegisterRequest req) {

        TokenPair tokenPair = auth.register(req);

        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, tokenPair.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path(REFRESH_COOKIE_PATH)
                .maxAge(Duration.ofSeconds(REFRESH_TOKEN_TTL))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new RegisterResponse(tokenPair.accessToken()));
    }

    @Operation(
            summary = "Аутентификация пользователя",
            description = """
                    Выполняет вход по username и паролю.
                    Возвращает access token в теле ответа.
                    Refresh token устанавливается в HttpOnly cookie.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный вход",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody(
                    description = "Данные для входа (логин и пароль)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody LoginRequest req) {

        TokenPair tokenPair = auth.login(req);

        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, tokenPair.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path(REFRESH_COOKIE_PATH)
                .maxAge(Duration.ofSeconds(REFRESH_TOKEN_TTL))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(tokenPair.accessToken()));
    }

    @Operation(
            summary = "Выход пользователя",
            description = """
                    Инвалидирует refresh token и очищает cookie.
                    Access token после выхода становится бесполезным (при следующем refresh будет ошибка).
                    """,
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный выход (cookie очищено)"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String header,
            @CookieValue(value = REFRESH_COOKIE_NAME, required = false) String refreshToken) {

        auth.logout(header, refreshToken);

        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path(REFRESH_COOKIE_PATH)
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
