package com.example.bankcards.exception.handler;

import com.example.bankcards.dto.common.ApiErrorResponse;
import com.example.bankcards.exception.auth.ProhibitedException;
import com.example.bankcards.exception.card.CardAlreadyHasPendingBlockRequestException;
import com.example.bankcards.exception.card.CardNotFoundOrForbiddenException;
import com.example.bankcards.exception.user.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            String message
                                                          ) {
        ApiErrorResponse body = new ApiErrorResponse(
                error,
                message,
                status.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "User not found", ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleSpringUserNotFound(UsernameNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "User not found", ex.getMessage());
    }

    @ExceptionHandler(CardAlreadyHasPendingBlockRequestException.class)
    public ResponseEntity<ApiErrorResponse> handlePendingBlock(CardAlreadyHasPendingBlockRequestException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Block request conflict", ex.getMessage());
    }

    @ExceptionHandler(CardNotFoundOrForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handleCard(CardNotFoundOrForbiddenException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Card access error", ex.getMessage());
    }

    @ExceptionHandler(ProhibitedException.class)
    public ResponseEntity<ApiErrorResponse> handleProhibited(ProhibitedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Action not allowed", ex.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse> handleJwt(JwtException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "JWT error", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad request", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", "Validation failed");
        body.put("errors", errors);
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(body);
    }
}