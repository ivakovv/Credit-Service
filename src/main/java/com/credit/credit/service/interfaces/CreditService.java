package com.credit.credit.service.interfaces;

import com.credit.credit.dto.credit.CreateCreditRequestDto;
import com.credit.credit.dto.credit.CreditResponseDto;


public interface CreditService {
    CreditResponseDto createCredit(CreateCreditRequestDto request);

    CreditResponseDto getCredit(Long id);
}
