package com.credit.credit.service;

import com.credit.credit.dto.credit.CreateCreditRequestDto;
import com.credit.credit.dto.credit.CreditResponseDto;
import com.credit.credit.entity.Client;
import com.credit.credit.entity.Credit;
import com.credit.credit.entity.RequestConditions;
import com.credit.credit.enums.CreditStatus;
import com.credit.credit.enums.CreditType;
import com.credit.credit.repository.ClientRepository;
import com.credit.credit.repository.CreditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientServiceImpl clientService;

    @InjectMocks
    private CreditServiceImpl creditService;

    private final Long CLIENT_ID = 1L;
    private final Long CREDIT_ID = 1L;
    private final String BUSINESS_TRANSACTION_ID = "txn-12345";
    private final Integer VERSION = 1;
    private final CreditType CREDIT_TYPE = CreditType.COOP_CREDIT;
    private final CreditStatus CREDIT_STATUS = CreditStatus.NEW;
    private final BigDecimal AMOUNT = BigDecimal.valueOf(100000);
    private final BigDecimal RATE = BigDecimal.valueOf(12.5);
    private final Integer TERM = 12;
    private final List<String> FEATURE_LABELS = List.of("fast_approval");

    @Test
    void createCredit_Success_NewCredit() {
        // Arrange
        RequestConditions requestConditions = new RequestConditions();
        requestConditions.setAmount(AMOUNT);
        requestConditions.setRate(RATE);
        requestConditions.setTerm(TERM);
        requestConditions.setFeatureLabels(FEATURE_LABELS);

        CreateCreditRequestDto request = new CreateCreditRequestDto(
                VERSION,
                CREDIT_TYPE,
                requestConditions,
                CLIENT_ID
        );

        Client client = new Client();
        client.setId(CLIENT_ID);
        client.setCredits(new ArrayList<>());

        Credit expectedCredit = new Credit();
        expectedCredit.setId(CREDIT_ID);
        expectedCredit.setVersion(VERSION);
        expectedCredit.setType(CREDIT_TYPE);
        expectedCredit.setStatus(CREDIT_STATUS);
        expectedCredit.setRequestConditions(requestConditions);
        expectedCredit.setClient(client);

        when(clientService.getClient(CLIENT_ID)).thenReturn(client);
        when(creditRepository.findByClientIdAndRequestConditionsAmountAndRequestConditionsTerm(
                CLIENT_ID, AMOUNT, TERM)).thenReturn(Optional.empty());
        when(creditRepository.save(any(Credit.class))).thenAnswer(invocation -> {
            Credit savedCredit = invocation.getArgument(0);
            savedCredit.setId(CREDIT_ID);
            return savedCredit;
        });
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Act
        CreditResponseDto result = creditService.createCredit(request);

        // Assert
        assertNotNull(result);
        assertEquals(CREDIT_ID, result.id()); // Проверяем ID
        assertEquals(BUSINESS_TRANSACTION_ID, result.businessTransactionId());
        assertEquals(VERSION, result.version());
        assertEquals(CREDIT_TYPE, result.type());
        assertEquals(CREDIT_STATUS, result.status());
        assertEquals(requestConditions, result.requestConditions());
        assertEquals(CLIENT_ID, result.clientId());

        verify(clientService).getClient(CLIENT_ID);
        verify(creditRepository).findByClientIdAndRequestConditionsAmountAndRequestConditionsTerm(
                CLIENT_ID, AMOUNT, TERM);
        verify(creditRepository).save(any(Credit.class));
        verify(clientRepository).save(client);
    }

    @Test
    void createCredit_Success_ExistingCredit() {
        // Arrange
        RequestConditions requestConditions = new RequestConditions();
        requestConditions.setAmount(AMOUNT);
        requestConditions.setRate(RATE);
        requestConditions.setTerm(TERM);
        requestConditions.setFeatureLabels(FEATURE_LABELS);

        CreateCreditRequestDto request = new CreateCreditRequestDto(
                VERSION,
                CREDIT_TYPE,
                requestConditions,
                CLIENT_ID
        );

        Client client = new Client();
        client.setId(CLIENT_ID);

        Credit existingCredit = new Credit();
        existingCredit.setId(CREDIT_ID);
        existingCredit.setVersion(1);
        existingCredit.setType(CREDIT_TYPE);
        existingCredit.setStatus(CREDIT_STATUS);
        existingCredit.setRequestConditions(requestConditions);
        existingCredit.setClient(client);

        when(clientService.getClient(CLIENT_ID)).thenReturn(client);
        when(creditRepository.findByClientIdAndRequestConditionsAmountAndRequestConditionsTerm(
                CLIENT_ID, AMOUNT, TERM)).thenReturn(Optional.of(existingCredit));

        // Act
        CreditResponseDto result = creditService.createCredit(request);

        // Assert
        assertNotNull(result);
        assertEquals(CREDIT_ID, result.id());
        assertEquals("txn-54321", result.businessTransactionId());
        assertEquals(CREDIT_TYPE, result.type());
        assertEquals(CREDIT_STATUS, result.status());
        assertEquals(requestConditions, result.requestConditions());
        assertEquals(CLIENT_ID, result.clientId());

        verify(clientService).getClient(CLIENT_ID);
        verify(creditRepository).findByClientIdAndRequestConditionsAmountAndRequestConditionsTerm(
                CLIENT_ID, AMOUNT, TERM);
        verify(creditRepository, never()).save(any(Credit.class));
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void createCredit_WhenClientNotFound_ThrowsNotFound() {
        // Arrange
        RequestConditions requestConditions = new RequestConditions();
        requestConditions.setAmount(AMOUNT);
        requestConditions.setRate(RATE);
        requestConditions.setTerm(TERM);
        requestConditions.setFeatureLabels(FEATURE_LABELS);

        CreateCreditRequestDto request = new CreateCreditRequestDto(
                VERSION,
                CREDIT_TYPE,
                requestConditions,
                CLIENT_ID
        );

        when(clientService.getClient(CLIENT_ID)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> creditService.createCredit(request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Пользователь не найден", exception.getReason());

        verify(clientService).getClient(CLIENT_ID);
        verify(creditRepository, never()).findByClientIdAndRequestConditionsAmountAndRequestConditionsTerm(
                any(), any(), any());
        verify(creditRepository, never()).save(any());
    }

    @Test
    void getCredit_Success() {
        // Arrange
        Credit credit = new Credit();
        credit.setId(CREDIT_ID);
        credit.setVersion(VERSION);
        credit.setType(CREDIT_TYPE);
        credit.setStatus(CREDIT_STATUS);

        RequestConditions requestConditions = new RequestConditions();
        requestConditions.setAmount(AMOUNT);
        requestConditions.setRate(RATE);
        requestConditions.setTerm(TERM);
        requestConditions.setFeatureLabels(FEATURE_LABELS);
        credit.setRequestConditions(requestConditions);

        Client client = new Client();
        client.setId(CLIENT_ID);
        credit.setClient(client);

        when(creditRepository.findById(CREDIT_ID)).thenReturn(Optional.of(credit));

        // Act
        CreditResponseDto result = creditService.getCredit(CREDIT_ID);

        // Assert
        assertNotNull(result);
        assertEquals(CREDIT_ID, result.id());
        assertEquals(BUSINESS_TRANSACTION_ID, result.businessTransactionId());
        assertEquals(VERSION, result.version());
        assertEquals(CREDIT_TYPE, result.type());
        assertEquals(CREDIT_STATUS, result.status());
        assertEquals(requestConditions, result.requestConditions());
        assertEquals(CLIENT_ID, result.clientId());

        verify(creditRepository).findById(CREDIT_ID);
    }

    @Test
    void getCredit_WhenCreditNotFound_ThrowsNotFound() {
        // Arrange
        when(creditRepository.findById(CREDIT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> creditService.getCredit(CREDIT_ID));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Кредит не найден", exception.getReason());

        verify(creditRepository).findById(CREDIT_ID);
    }
}
