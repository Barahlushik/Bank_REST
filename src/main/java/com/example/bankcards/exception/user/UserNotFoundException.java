package com.example.bankcards.exception.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }
}