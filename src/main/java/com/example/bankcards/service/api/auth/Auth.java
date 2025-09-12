package com.example.bankcards.service.api.auth;

import com.example.bankcards.dto.auth.*;

public interface Auth {
     TokenPair register(RegisterRequest req);
     TokenPair login(LoginRequest req);
    void logout(String access, String refresh);
}
