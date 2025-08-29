package com.example.bankcards.controller;

import com.example.bankcards.dto.UserLoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base-path}/auth")
@Validated
public class SecurityController {

    @PostMapping("/register")
    public ResponseEntity<?> register() {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequest req) {}

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {}

    /**
     * *
     * Tech endpoint
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {}

}
