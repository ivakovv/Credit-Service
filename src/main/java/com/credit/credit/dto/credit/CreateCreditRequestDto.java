package com.credit.credit.dto.credit;

import com.credit.credit.entity.RequestConditions;
import com.credit.credit.enums.CreditType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCreditRequestDto(
        @NotBlank(message = "Идентификатор бизнес-транзакции не может быть пустым")
        String businessTransactionId,

        @Min(value = 1, message = "Версия должна быть не менее 1")
        Integer version,

        @NotNull(message = "Требуется указать тип кредита")
        CreditType type,

        @Valid @NotNull(message = "Требуются условия запроса")
        RequestConditions requestConditions,

        @NotNull(message = "Id клиента не может быть пустым")
        Long clientId
) {
}
