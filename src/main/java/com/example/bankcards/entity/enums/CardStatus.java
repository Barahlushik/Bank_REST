package com.example.bankcards.entity.enums;

import lombok.Getter;

public enum CardStatus {
    ACTIVE(false),
    BLOCKED(true),
    EXPIRED(true);

    @Getter
    private final boolean lockedForTransfer;

    CardStatus(boolean lockedForTransfer) {
        this.lockedForTransfer = lockedForTransfer;
    }
}
