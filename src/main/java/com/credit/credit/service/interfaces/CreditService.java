package com.credit.credit.service.interfaces;

import com.credit.credit.dto.credit.CreateCreditRequestDto;
import com.credit.credit.dto.credit.CreditResponseDto;


public interface CreditService {
    /**
     * @author Ivakov Andrey
     * <p>
     *     <i> <b> Метод создает новый кредит </b> </i>
     * </p>
     * @param request: dto для создания кредита
     * @return кредит
     * @see CreditResponseDto
     * Если такой кредит уже существует, то возращается, не создается новый
     * Если кредита нет, то создается и возращается
     */
    CreditResponseDto createCredit(CreateCreditRequestDto request);

    /**
     * @author Ivakov Andrey
     *      * <p>
     *      *     <i> <b> Метод возращает кредит </b> </i>
     *      * </p>
     * @param id: id кредита
     * @return кредит
     * @see CreditResponseDto
     */
    CreditResponseDto getCredit(Long id);
}
