package com.example.bankcards.entity;

import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.CardType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="cards")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false, length = 30)
    CardType type;

    @Column(name = "number", nullable = false, length = 64)
    String number;

    @Column(name = "expiration", nullable = false)
    LocalDate expiration;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    CardStatus status;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    BigDecimal balance;

    @Column(nullable = false, precision = 19, scale = 2)
    BigDecimal hold;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    boolean isDeleted = false;

    @Version
    Long version;

    @PrePersist @PreUpdate
    void normalizeMoney() {
        balance = norm(balance);
        hold    = norm(hold);
    }
    private static BigDecimal norm(BigDecimal v) {
        if (v == null) return BigDecimal.ZERO.setScale(2);
        return v.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Card other = (Card) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Card.class.hashCode();
    }
}
