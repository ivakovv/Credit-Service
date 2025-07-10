package com.credit.credit.controller;

import com.credit.credit.dto.CreateClientRequestDto;
import com.credit.credit.dto.UpdateClientRequestDto;
import com.credit.credit.entity.Client;
import com.credit.credit.service.ClientServiceImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientServiceImpl clientService;
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Клиент успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверный формат данных"),
            @ApiResponse(responseCode = "409", description = "Данный клиент уже существует"),
            @ApiResponse(responseCode = "500", description = "Сервер временно недоступен")
    })
    @PostMapping()
    public ResponseEntity<Client> createUser(@RequestBody @Valid CreateClientRequestDto request) {
        return ResponseEntity.ok(clientService.createClient(request));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о клиенте обновлена"),
            @ApiResponse(responseCode = "400", description = "Неверный формат данных"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "500", description = "Сервер временно недоступен")
    })
    @PatchMapping("/{client_id}")
    public ResponseEntity<Client> updateClient(@PathVariable("client_id") Long id,
                                               @RequestBody @Valid UpdateClientRequestDto request){
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }

    @GetMapping("/{client_id}")
    public ResponseEntity<Client> getClient(@PathVariable("client_id") Long id){
        return ResponseEntity.ok(clientService.getClient(id));
    }


}
