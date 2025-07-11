package com.credit.credit.dto.credit;

import com.credit.credit.entity.Credit;
import com.credit.credit.entity.RequestConditions;
import com.credit.credit.enums.CreditStatus;
import com.credit.credit.enums.CreditType;

import java.time.OffsetDateTime;

public record CreditResponseDto(
        Long id,
        String businessTransactionId,
        Integer version,
        CreditType type,
        CreditStatus status,
        RequestConditions requestConditions,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Long clientId
) {
    public static CreditResponseDto fromEntity(Credit credit) {
        return new CreditResponseDto(
                credit.getId(),
                credit.getBusinessTransactionId(),
                credit.getVersion(),
                credit.getType(),
                credit.getStatus(),
                credit.getRequestConditions(),
                credit.getCreatedAt(),
                credit.getUpdatedAt(),
                credit.getClient() != null ? credit.getClient().getId() : null
        );
    }
}
