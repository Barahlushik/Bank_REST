package com.example.bankcards.util;

public final class SecurityConstants {
    private SecurityConstants() {}
    public static final String REFRESH_COOKIE_NAME = "refreshToken";
    public static final String REFRESH_COOKIE_PATH = "/api/auth/jwt/refresh";
    public static final long ACCESS_TOKEN_TTL = 900;
    public static final long REFRESH_TOKEN_TTL = 500_000;
}