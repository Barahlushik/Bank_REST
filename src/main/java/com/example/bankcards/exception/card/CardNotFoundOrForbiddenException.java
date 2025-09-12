package com.example.bankcards.exception.card;


public class CardNotFoundOrForbiddenException extends RuntimeException {
    public CardNotFoundOrForbiddenException(Long cardId) {
        super("Card not found or forbidden: " + cardId);
    }
}