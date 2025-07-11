package com.credit.credit.repository;

import com.credit.credit.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    Optional<Credit> findById(Long id);

    Optional<Credit> findByClientIdAndRequestConditionsAmountAndRequestConditionsTerm(
            Long clientId,
            BigDecimal amount,
            Integer term
    );

}
