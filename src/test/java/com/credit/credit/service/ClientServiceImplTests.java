package com.credit.credit.service;

import com.credit.credit.dto.client.CreateClientRequestDto;
import com.credit.credit.dto.client.UpdateClientRequestDto;
import com.credit.credit.entity.Client;
import com.credit.credit.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private final String TEST_PHONE = "+79211234567";
    private final String TEST_FIRST_NAME = "Андрей";
    private final String TEST_LAST_NAME = "Иваков";
    private final String TEST_MIDDLE_NAME = "Валерьевич";
    private final String EXPECTED_FULL_NAME = "Иваков Андрей Валерьевич";

    @Test
    void createClient_Success() {
        // Arrange
        CreateClientRequestDto request = new CreateClientRequestDto(
                TEST_FIRST_NAME,
                TEST_LAST_NAME,
                TEST_MIDDLE_NAME,
                EXPECTED_FULL_NAME,
                TEST_PHONE
        );

        when(clientRepository.findByPhoneNumber(TEST_PHONE)).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client client = invocation.getArgument(0);
            client.setId(1L);
            return client;
        });

        // Act
        Client result = clientService.createClient(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(EXPECTED_FULL_NAME, result.getFullName());
        assertEquals(TEST_FIRST_NAME, result.getFirstName());
        assertEquals(TEST_LAST_NAME, result.getLastName());
        assertEquals(TEST_MIDDLE_NAME, result.getMiddleName());
        assertEquals(TEST_PHONE, result.getPhoneNumber());

        verify(clientRepository).findByPhoneNumber(TEST_PHONE);
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void updateClient_Success_AllFields() {
        // Arrange
        Long clientId = 1L;
        String updatedFirstName = "Антон";
        String updatedLastName = "Антонов";
        String updatedMiddleName = "Александрович";
        String updatedPhone = "+79219876543";
        String expectedUpdatedFullName = updatedLastName + " " + updatedFirstName + " " + updatedMiddleName;

        UpdateClientRequestDto request = new UpdateClientRequestDto(
                updatedFirstName,
                updatedLastName,
                updatedMiddleName,
                updatedPhone
        );

        Client existingClient = createTestClient(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Client result = clientService.updateClient(clientId, request);

        // Assert
        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals(expectedUpdatedFullName, result.getFullName());
        assertEquals(updatedFirstName, result.getFirstName());
        assertEquals(updatedLastName, result.getLastName());
        assertEquals(updatedMiddleName, result.getMiddleName());
        assertEquals(updatedPhone, result.getPhoneNumber());

        verify(clientRepository).findById(clientId);
        verify(clientRepository).save(existingClient);
    }

    @Test
    void updateClient_Success_PartialFields() {
        // Arrange
        Long clientId = 1L;
        String updatedFirstName = "Сергей";
        String expectedUpdatedFullName = TEST_LAST_NAME + " " + updatedFirstName + " " + TEST_MIDDLE_NAME;

        UpdateClientRequestDto request = new UpdateClientRequestDto(
                updatedFirstName,
                null,
                null,
                null
        );

        Client existingClient = createTestClient(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Client result = clientService.updateClient(clientId, request);

        // Assert
        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals(expectedUpdatedFullName, result.getFullName());
        assertEquals(updatedFirstName, result.getFirstName());
        assertEquals(TEST_LAST_NAME, result.getLastName());
        assertEquals(TEST_MIDDLE_NAME, result.getMiddleName());
        assertEquals(TEST_PHONE, result.getPhoneNumber());

        verify(clientRepository).findById(clientId);
        verify(clientRepository).save(existingClient);
    }

    @Test
    void updateClient_WhenClientNotFound_ThrowsNotFound() {
        // Arrange
        Long clientId = 1L;
        UpdateClientRequestDto request = new UpdateClientRequestDto(
                "Иванов",
                "Иван",
                "Иванович",
                "+79219876543"
        );

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> clientService.updateClient(clientId, request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Пользователь не найден", exception.getReason());

        verify(clientRepository).findById(clientId);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void getClient_Success() {
        // Arrange
        Long clientId = 1L;
        Client expectedClient = createTestClient(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(expectedClient));

        // Act
        Client result = clientService.getClient(clientId);

        // Assert
        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals(EXPECTED_FULL_NAME, result.getFullName());
        verify(clientRepository).findById(clientId);
    }

    @Test
    void getClient_WhenClientNotFound_ThrowsNotFound() {
        // Arrange
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> clientService.getClient(clientId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Пользователь не найден", exception.getReason());
        verify(clientRepository).findById(clientId);
    }

    private Client createTestClient(Long id) {
        Client client = new Client();
        client.setId(id);
        client.setFirstName(TEST_FIRST_NAME);
        client.setLastName(TEST_LAST_NAME);
        client.setMiddleName(TEST_MIDDLE_NAME);
        client.setPhoneNumber(TEST_PHONE);
        client.setFullName(EXPECTED_FULL_NAME);
        return client;
    }
}
