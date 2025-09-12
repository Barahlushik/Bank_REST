package com.example.bankcards.service.impl.auth;

import com.example.bankcards.dto.auth.TokenPair;
import com.example.bankcards.entity.RefreshToken;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.RefreshTokenRepository;
import com.example.bankcards.service.api.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.bankcards.util.SecurityConstants.ACCESS_TOKEN_TTL;
import static com.example.bankcards.util.SecurityConstants.REFRESH_TOKEN_TTL;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {


    RefreshTokenRepository refreshTokenRepository;
    static String BLACKLIST_PREFIX = "jwt:blacklist:";
    RedisTemplate<String, String> redisTemplate;
    SecretKey key = Keys.hmacShaKeyFor("super-secret-jwt-key-must-be-very-long".getBytes());


    @Override
    public String generateAccessToken(User u) {
        return Jwts.builder()
                .setSubject(u.getUsername())
                .claim("roles",
                        List.of("ROLE_" + u.getRole().getRole().name()))
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(ACCESS_TOKEN_TTL)))
                .signWith(key)
                .compact();
    }

    @Override
    public String generateRefreshToken(User u) {
        String refT = Jwts.builder()
                .setSubject(u.getUsername())
                .claim("roles",
                        List.of("ROLE_" + u.getRole().getRole().name()))
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(REFRESH_TOKEN_TTL)))
                .signWith(key)
                .compact();
        saveRefreshToken(refT, u);
        return refT;
    }

    @Override
    public TokenPair generatePairOfTokens(User u) {
        String accT = generateAccessToken(u);
        String refT = generateRefreshToken(u);
        return new TokenPair(accT, refT);
    }

    private void saveRefreshToken(String refT, User u) {
        Claims claims = parseClaims(refT);
        String jti = claims.getId();
        Date issAt = claims.getIssuedAt();
        Date expAt = claims.getExpiration();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .user(u)
                .tokenId(jti)
                .secretHash(refT)
                .issuedAt(issAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .expiresAt(expAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public boolean validateAccessToken(String token) {
        Claims claims = parseClaims(token);
        if (claims.getExpiration()
                .before(new Date())) {
            return false;
        }
        String jti = claims.getId();
        return !isBlacklisted(jti);
    }

    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jti));
    }


    public void addToBlacklist(String jti, Date expiration) {
        long ttl = expiration.getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            redisTemplate.opsForValue()
                    .set(BLACKLIST_PREFIX + jti, "revoked", ttl, TimeUnit.MILLISECONDS);
        }
    }



    @Override
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = parseClaims(token);
            if (claims.getExpiration().before(new Date())) {
                return false;
            }
            String jti = claims.getId();
            RefreshToken stored = refreshTokenRepository.findByTokenId(jti)
                    .orElse(null);
            if (stored == null) return false;
            if (!stored.activeAt(LocalDateTime.now())) return false;
            return token.equals(stored.getSecretHash());

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    @Override
    public TokenPair refreshTokens(HttpServletRequest request) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie c : request.getCookies()) {
                if ("refreshToken".equals(c.getName())) {
                    refreshToken = c.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            throw new JwtException("Missing refresh token cookie");
        }

        if (!validateRefreshToken(refreshToken)) {
            throw new JwtException("Invalid refresh token");
        }

        Claims claims = parseClaims(refreshToken);
        String jti = claims.getId();

        RefreshToken stored = refreshTokenRepository.findByTokenId(jti)
                .orElseThrow(() -> new JwtException("Refresh token not found"));

        stored.setRevokedAt(LocalDateTime.now());
        stored.setRevokeReason("ROTATED");
        refreshTokenRepository.save(stored);
        return generatePairOfTokens(stored.getUser());
    }


    public void revokeAccessToken(String access) {
        Claims claims = parseClaims(access);
        String jti = claims.getId();
        Date expiration = claims.getExpiration();
        addToBlacklist(jti, expiration);
    }

    public void revokeRefreshToken(String refresh) {
        Claims claims = parseClaims(refresh);
        String jti = claims.getId();

        refreshTokenRepository.findByTokenId(jti).ifPresent(stored -> {
            stored.setRevokedAt(LocalDateTime.now());
            stored.setRevokeReason("LOGOUT");
            refreshTokenRepository.save(stored);
        });
    }

    // Exception password cannot be more than 72 bytes
    /*private String hashToken(String token) {
        return BCrypt.hashpw(token, BCrypt.gensalt());
    }*/

    @Override
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
