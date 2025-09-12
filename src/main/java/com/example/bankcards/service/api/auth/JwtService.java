package com.example.bankcards.service.api.auth;

import com.example.bankcards.dto.auth.TokenPair;
import com.example.bankcards.entity.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;


public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    TokenPair generatePairOfTokens(User user);
    boolean validateAccessToken(String token);
    boolean validateRefreshToken(String token);
    TokenPair refreshTokens(HttpServletRequest refreshToken);
    void revokeAccessToken(String access);
    void revokeRefreshToken(String refresh);
    Claims parseClaims(String token);
}
