package com.example.bankcards.service.api.card;

import com.example.bankcards.dto.admin.*;
import com.example.bankcards.dto.admin.create.CreateCardRequest;
import com.example.bankcards.dto.admin.create.CreateCardResponse;
import com.example.bankcards.dto.admin.update.UpdateCardRequest;
import com.example.bankcards.dto.admin.update.UpdateCardResponse;
import com.example.bankcards.dto.common.CardDto;
import com.example.bankcards.entity.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminCardService {

    Page<CardDto> findAll(CardFilter filter, Pageable pageable);

    CreateCardResponse create(CreateCardRequest req);

    UpdateCardResponse update(UpdateCardRequest req);

    SetCardStatusResponse setStatus(Long cardId, CardStatus status);

    void delete(Long cardId);

}
