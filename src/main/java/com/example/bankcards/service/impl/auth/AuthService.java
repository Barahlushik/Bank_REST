package com.example.bankcards.service.impl.auth;

import com.example.bankcards.dto.auth.*;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.RoleType;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.api.auth.Auth;
import com.example.bankcards.service.api.auth.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.antlr.v4.runtime.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthService implements Auth {

    JwtService jwtService;
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public TokenPair register(RegisterRequest req) {
        if (userRepository.findByUsername(req.username()).isPresent()) {
            throw new IllegalArgumentException("User already exists: " + req.username());
        }

        String hashedPassword = passwordEncoder.encode(req.password());
        Role defaultRole = roleRepository.getByRole(RoleType.USER)
                .orElseThrow(() -> new IllegalStateException("Default role USER not found"));

        User newUser = User.builder()
                .username(req.username())
                .passwordHash(hashedPassword)
                .name(req.name())
                .surname(req.surname())
                .birthDate(req.birthDate())
                .role(defaultRole)
                .build();

        userRepository.save(newUser);
        return jwtService.generatePairOfTokens(newUser);
    }

    @Override
    public TokenPair login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
                                          );
        User u = userRepository.findByUsername(req.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + req.username()));
        return jwtService.generatePairOfTokens(u);
    }

    @Override
    public void logout(String access, String refresh) {
        jwtService.revokeAccessToken(access);
        jwtService.revokeRefreshToken(refresh);
    }

}
