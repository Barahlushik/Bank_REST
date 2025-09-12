package com.example.bankcards.entity.enums;

import lombok.Getter;


@Getter
public enum CardType {
    VISA("4", 16),
    MASTERCARD("5", 16),
    AMEX("34", 15),
    BANK_SPECIFIC("2202", 16);

    private final String prefix;
    private final int length;

    CardType(String prefix, int length) {
        this.prefix = prefix;
        this.length = length;
    }
}
