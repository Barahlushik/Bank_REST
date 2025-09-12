package com.example.bankcards.exception.card;

public class CardAlreadyHasPendingBlockRequestException extends RuntimeException {
    public CardAlreadyHasPendingBlockRequestException(Long cardId) {
        super("Card " + cardId + " already has a pending block request");
    }
}
