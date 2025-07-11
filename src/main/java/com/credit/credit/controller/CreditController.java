package com.credit.credit.controller;

import com.credit.credit.dto.credit.CreateCreditRequestDto;
import com.credit.credit.dto.credit.CreditResponseDto;
import com.credit.credit.service.CreditServiceImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/credits")
public class CreditController {

    private final CreditServiceImpl creditService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о кридите получена"),
            @ApiResponse(responseCode = "404", description = "Кредит не найден"),
            @ApiResponse(responseCode = "500", description = "Сервер временно недоступен")
    })
    @GetMapping("/{credit_id}")
    public ResponseEntity<CreditResponseDto> getCredit(@PathVariable("credit_id") Long id){
        return ResponseEntity.ok(creditService.getCredit(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Кредит успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный формат данных"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "500", description = "Сервер временно недоступен")
    })
    @PostMapping()
    public ResponseEntity<CreditResponseDto> createCredit(@RequestBody @Valid CreateCreditRequestDto request){
        return ResponseEntity.ok(creditService.createCredit(request));
    }
}
