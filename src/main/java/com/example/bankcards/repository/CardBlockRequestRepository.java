package com.example.bankcards.repository;

import com.example.bankcards.entity.CardBlockRequestEntity;
import com.example.bankcards.entity.enums.CardBlockRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CardBlockRequestRepository extends JpaRepository<CardBlockRequestEntity, Long> {

    @Query("select r from CardBlockRequestEntity r " +
            "where r.card.id = :cardId and r.status = :status")
    Optional<CardBlockRequestEntity> findByCardIdAndStatus(Long cardId, CardBlockRequestStatus status);
}
