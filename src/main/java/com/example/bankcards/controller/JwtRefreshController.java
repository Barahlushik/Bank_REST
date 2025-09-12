package com.example.bankcards.controller;


import com.example.bankcards.dto.auth.RefreshedAccessToken;
import com.example.bankcards.dto.auth.TokenPair;
import com.example.bankcards.service.api.auth.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

import static com.example.bankcards.util.SecurityConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/jwt")
@Tag(name = "Token refresh", description = "Обновление access токена с помощью refresh токена (HttpOnly cookie)")
public class JwtRefreshController {

    private final JwtService jwt;

    @Operation(
            summary = "Обновить access токен",
            description = """
                    Обновляет access токен, используя refresh токен из HttpOnly cookie.
                    Возвращает новый access токен в теле ответа, а refresh токен автоматически
                    обновляется и отправляется обратно в cookie.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Новый access токен выдан",
                            content = @Content(schema = @Schema(implementation = RefreshedAccessToken.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Refresh токен отсутствует, недействителен или истёк"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Внутренняя ошибка сервера"
                    )
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<RefreshedAccessToken> refreshToken(HttpServletRequest request) {
        TokenPair pair = jwt.refreshTokens(request);

        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, pair.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path(REFRESH_COOKIE_PATH)
                .maxAge(Duration.ofSeconds(REFRESH_TOKEN_TTL))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new RefreshedAccessToken(pair.accessToken()));
    }
}