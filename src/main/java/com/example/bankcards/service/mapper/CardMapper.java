package com.example.bankcards.service.mapper;

import com.example.bankcards.dto.admin.SetCardStatusResponse;
import com.example.bankcards.dto.admin.create.CreateCardResponse;
import com.example.bankcards.dto.admin.update.UpdateCardResponse;
import com.example.bankcards.dto.common.CardDto;
import com.example.bankcards.dto.user.BalanceResponse;
import com.example.bankcards.dto.user.CardBlockResponse;
import com.example.bankcards.dto.user.TransferBetweenMyCardsResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;


@Mapper(componentModel = "spring")
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    @Mapping(target = "number", expression = "java(maskCardNumber(card.getNumber()))")
    CardDto toDto(Card card);

    @Mapping(target = "number", expression = "java(maskCardNumber(card.getNumber()))")
    CreateCardResponse toCreateResponse(Card card);

    @Mapping(target = "number", expression = "java(maskCardNumber(card.getNumber()))")
    UpdateCardResponse toUpdateResponse(Card card);

    @Mapping(target = "oldStatus", source = "oldStatus")
    SetCardStatusResponse toSetStatusResponse(Card card, CardStatus oldStatus);

    BalanceResponse toBalanceResponse(Card card);

    @Mapping(target = "status", constant = "SUCCESS")
    TransferBetweenMyCardsResponse toTransferResponse(Long fromCardId,
                                                      Long toCardId,
                                                      BigDecimal amount);

    @Mapping(target = "message", source = "message")
    CardBlockResponse toCardBlockResponse(Card card, String message);

    default String maskCardNumber(String number) {
        if (number == null || number.length() < 4) return "****";
        return "**** **** **** " + number.substring(number.length() - 4);
    }
}