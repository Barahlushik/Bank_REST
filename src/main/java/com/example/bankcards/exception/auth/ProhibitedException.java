package com.example.bankcards.exception.auth;

public class ProhibitedException extends RuntimeException {
    public ProhibitedException(Long userId) {
        super("User " + userId + " is not allowed to perform this action");
    }
}