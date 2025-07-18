package com.credit.credit.service;

import com.credit.credit.dto.client.CreateClientRequestDto;
import com.credit.credit.dto.client.UpdateClientRequestDto;
import com.credit.credit.entity.Client;
import com.credit.credit.repository.ClientRepository;
import com.credit.credit.exception.ClientAlreadyExistsException;
import com.credit.credit.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.credit.credit.mapper.ClientMapper;
import com.credit.credit.util.NameUtil;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private NameUtil nameUtil;

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
        Client mappedClient = createTestClient(null);
        when(clientMapper.toClient(request)).thenReturn(mappedClient);
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
    void createClient_WhenClientAlreadyExists_ThrowsClientAlreadyExistsException() {
        // Arrange
        CreateClientRequestDto request = new CreateClientRequestDto(
                TEST_FIRST_NAME,
                TEST_LAST_NAME,
                TEST_MIDDLE_NAME,
                EXPECTED_FULL_NAME,
                TEST_PHONE
        );
        Client existingClient = createTestClient(1L);
        when(clientRepository.findByPhoneNumber(TEST_PHONE)).thenReturn(Optional.of(existingClient));

        // Act & Assert
        ClientAlreadyExistsException exception = assertThrows(ClientAlreadyExistsException.class,
                () -> clientService.createClient(request));
        assertTrue(exception.getMessage().contains(TEST_PHONE));
        verify(clientRepository).findByPhoneNumber(TEST_PHONE);
        verify(clientRepository, never()).save(any(Client.class));
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
        // Мокаем NameUtil
        doAnswer(invocation -> {
            Client client = invocation.getArgument(0);
            String firstName = invocation.getArgument(1);
            String lastName = invocation.getArgument(2);
            String middleName = invocation.getArgument(3);
            if (firstName != null) client.setFirstName(firstName);
            if (lastName != null) client.setLastName(lastName);
            if (middleName != null) client.setMiddleName(middleName);
            return null;
        }).when(nameUtil).updateNameFields(any(Client.class), any(), any(), any());
        when(nameUtil.isNameUpdated(updatedFirstName, updatedLastName, updatedMiddleName)).thenReturn(true);
        when(nameUtil.generateFullName(anyString(), anyString(), anyString())).thenReturn(expectedUpdatedFullName);

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
        doAnswer(invocation -> {
            Client client = invocation.getArgument(0);
            String firstName = invocation.getArgument(1);
            String lastName = invocation.getArgument(2);
            String middleName = invocation.getArgument(3);
            if (firstName != null) client.setFirstName(firstName);
            if (lastName != null) client.setLastName(lastName);
            if (middleName != null) client.setMiddleName(middleName);
            return null;
        }).when(nameUtil).updateNameFields(any(Client.class), any(), any(), any());
        when(nameUtil.isNameUpdated(updatedFirstName, null, null)).thenReturn(true);
        when(nameUtil.generateFullName(anyString(), anyString(), anyString())).thenReturn(expectedUpdatedFullName);

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
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> clientService.updateClient(clientId, request));
        assertTrue(exception.getMessage().contains("Client"));
        assertTrue(exception.getMessage().contains(clientId.toString()));
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
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> clientService.getClient(clientId));
        assertTrue(exception.getMessage().contains("Client"));
        assertTrue(exception.getMessage().contains(clientId.toString()));
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
