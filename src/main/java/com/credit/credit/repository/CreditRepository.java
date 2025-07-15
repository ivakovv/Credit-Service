package com.credit.credit.repository;

import com.credit.credit.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    Optional<Credit> findById(Long id);

    Optional<Credit> findByClientIdAndRequestConditionsAmountAndRequestConditionsTerm(
            Long clientId,
            BigDecimal amount,
            Integer term
    );

}
