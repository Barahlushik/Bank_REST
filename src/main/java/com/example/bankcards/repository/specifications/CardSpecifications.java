package com.example.bankcards.repository.specifications;

import com.example.bankcards.dto.admin.CardFilter;
import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.domain.Specification;

public class CardSpecifications {

    public static Specification<Card> withFilter(CardFilter filter) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (filter.status() != null) {
                predicates.getExpressions().add(cb.equal(root.get("status"), filter.status()));
            }

            if (filter.type() != null) {
                predicates.getExpressions().add(cb.equal(root.get("type"), filter.type()));
            }

            if (filter.expirationBefore() != null) {
                predicates.getExpressions().add(cb.lessThan(root.get("expiration"), filter.expirationBefore()));
            }

            if (filter.expirationAfter() != null) {
                predicates.getExpressions().add(cb.greaterThan(root.get("expiration"), filter.expirationAfter()));
            }

            predicates.getExpressions().add(cb.isFalse(root.get("isDeleted")));

            return predicates;
        };
    }
}
