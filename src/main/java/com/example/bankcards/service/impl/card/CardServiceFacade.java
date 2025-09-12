package com.example.bankcards.service.impl.card;

import com.example.bankcards.dto.admin.CardFilter;
import com.example.bankcards.dto.admin.SetCardStatusResponse;
import com.example.bankcards.dto.admin.create.CreateCardRequest;
import com.example.bankcards.dto.admin.create.CreateCardResponse;
import com.example.bankcards.dto.admin.update.UpdateCardRequest;
import com.example.bankcards.dto.admin.update.UpdateCardResponse;
import com.example.bankcards.dto.common.CardDto;
import com.example.bankcards.dto.user.*;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequestEntity;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardBlockRequestStatus;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;
import com.example.bankcards.exception.card.CardAlreadyHasPendingBlockRequestException;
import com.example.bankcards.exception.card.CardNotFoundOrForbiddenException;
import com.example.bankcards.exception.user.UserNotFoundException;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.specifications.CardSpecifications;
import com.example.bankcards.service.api.card.CardService;
import com.example.bankcards.service.mapper.CardMapper;
import com.example.bankcards.util.CardNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CardServiceFacade implements CardService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CardBlockRequestRepository cardBlockRequestRepository;
    private final CardMapper cardMapper;


    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> findAll(CardFilter filter, Pageable pageable) {
        return cardRepository.findAll(CardSpecifications.withFilter(filter), pageable)
                .map(cardMapper::toDto);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CreateCardResponse create(CreateCardRequest req) {
        Long ownerId = req.ownerId();
        CardType cardType = req.type();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(UserNotFoundException::new);
        String newCardNumber = CardNumber.generate(req.type());
        LocalDate expiration = LocalDate.now()
                .plusYears(4);

        Card card = Card.builder()
                .type(cardType)
                .number(newCardNumber)
                .owner(owner)
                .status(CardStatus.ACTIVE)
                .expiration(expiration)
                .isDeleted(false)
                .build();

        return cardMapper.toCreateResponse(cardRepository.save(card));

    }

    @Override
    public UpdateCardResponse update(UpdateCardRequest req) {
        Card card = cardRepository.findById(req.cardId())
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        card.setBalance(req.balance());
        card.setExpiration(req.expiration());
        Card updated = cardRepository.save(card);
        return cardMapper.toUpdateResponse(updated);
    }

    @Override
    @Transactional
    public SetCardStatusResponse setStatus(Long cardId, CardStatus status) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        CardStatus oldStatus = card.getStatus();
        card.setStatus(status);
        Card saved = cardRepository.save(card);
        return cardMapper.toSetStatusResponse(saved, oldStatus);
    }

    @Override
    @Transactional
    public void delete(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        card.setDeleted(true);
        cardRepository.save(card);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getCardsByUsername(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return cardRepository.findAllByOwner_Id(user.getId(), pageable)
                .map(cardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceResponse getCardBalance(String username, Long cardId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!cardRepository.existsByIdAndOwnerId(cardId, user.getId())) {
            throw new CardNotFoundOrForbiddenException(cardId);
        }

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundOrForbiddenException(cardId));

        return new BalanceResponse(card.getId(), card.getBalance());
    }


    @Transactional
    public TransferBetweenMyCardsResponse transfer(String username,
                                                   TransferBetweenMyCardsRequest req) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Card fromCard = cardRepository.findById(req.fromCardId())
                .orElseThrow(() -> new CardNotFoundOrForbiddenException(req.fromCardId()));

        if (!fromCard.getOwner().getId().equals(user.getId())) {
            throw new CardNotFoundOrForbiddenException(req.fromCardId());
        }

        Card toCard = cardRepository.findById(req.toCardId())
                .orElseThrow(() -> new CardNotFoundOrForbiddenException(req.toCardId()));

        if (!toCard.getOwner().getId().equals(user.getId())) {
            throw new CardNotFoundOrForbiddenException(req.toCardId());
        }

        validateCard(fromCard);
        validateCard(toCard);
        validateBalance(fromCard, req.amount());
        fromCard.setBalance(fromCard.getBalance().subtract(req.amount()));
        toCard.setBalance(toCard.getBalance().add(req.amount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        return new TransferBetweenMyCardsResponse(
                fromCard.getId(),
                toCard.getId(),
                req.amount(),
                "SUCCESS"
        );
    }

    private void validateCard(Card card) {
        if (card.isDeleted()) {
            throw new IllegalStateException("Card " + card.getId() + " is deleted");
        }
        if (card.getStatus().isLockedForTransfer()) {
            throw new IllegalStateException("Card " + card.getId() + " is blocked/expired");
        }
        if (card.getExpiration().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Card " + card.getId() + " expired");
        }
    }

    private void validateBalance(Card fromCard, BigDecimal amount) {
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Not enough funds on card " + fromCard.getId());
        }
    }
    @Override
    @Transactional
    public CardBlockResponse requestBlock(String username, Long cardId, CardBlockRequest req) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundOrForbiddenException(cardId));

        if (!Objects.equals(card.getOwner().getId(), user.getId())) {
            throw new CardNotFoundOrForbiddenException(cardId);
        }

        cardBlockRequestRepository.findByCardIdAndStatus(cardId, CardBlockRequestStatus.PENDING)
                .ifPresent(r -> {
                    throw new CardAlreadyHasPendingBlockRequestException(cardId);
                });


        CardBlockRequestEntity requestEntity = CardBlockRequestEntity.builder()
                .card(card)
                .requestedBy(user)
                .reason(req.reason())
                .status(CardBlockRequestStatus.PENDING)
                .build();

        cardBlockRequestRepository.save(requestEntity);

        return new CardBlockResponse(
                card.getId(),
                "Block request created and awaiting approval"
        );
    }

}
