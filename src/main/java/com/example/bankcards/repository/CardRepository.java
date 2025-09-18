package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardType;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {


    @Query("select c.owner.id from Card c where c.id = :cardId")
    Optional<Long> getOwnerIdById(@Param("cardId") Long cardId);


    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Card c WHERE c.id = :cardId AND (c.status = 'BLOCKED' OR c.status = 'EXPIRED')")
    boolean isBlockedOrExpired(@NotNull @Param("cardId") Long cardId);

    @Query("SELECT c.id FROM Card c WHERE c.number = :encryptedCardNumber")
    Optional<Long> findIdByEncryptedCardNumber(@Param("encryptedCardNumber") String encryptedCardNumber);


    @Query("SELECT c.id FROM Card c WHERE c.number = :cardNumber")
    Optional<Long> findIdByNumber(@Param("cardNumber") String cardNumber);


    @Modifying
    @Query("UPDATE Card c SET c.balance = c.balance + :amount WHERE c.id = :cardId AND c.status = 'ACTIVE'")
    int addToBalance(@Param("cardId") Long cardId, @Param("amount") BigDecimal amount);


    boolean existsByIdAndOwnerId(@NotNull Long id, @NotNull Long ownerId);

    Page<Card> findAllByOwner_Id(Long ownerId, Pageable pageable);

    Page<Card> findCardsByExpirationBeforeAndStatusNot(Pageable pageable, LocalDate expirationDate, String status);



}
