package com.example.bankcards.service.api.card;

import com.example.bankcards.dto.common.CardDto;
import com.example.bankcards.dto.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCardService {

     Page<CardDto> getCardsByUsername(String username, Pageable pageable);

     BalanceResponse getCardBalance(String username, Long cardId);

     TransferBetweenMyCardsResponse transfer(String username,
                                             TransferBetweenMyCardsRequest request);

     CardBlockResponse requestBlock(String username, Long cardId, CardBlockRequest request);

}
