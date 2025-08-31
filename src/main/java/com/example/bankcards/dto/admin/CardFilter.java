package com.example.bankcards.dto.admin;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;

import java.time.LocalDate;

public record CardFilter(
        CardStatus status,
        CardType type,
        LocalDate expirationBefore,
        LocalDate expirationAfter
) {}
