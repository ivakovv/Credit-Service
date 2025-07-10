package com.credit.credit.service;

import com.credit.credit.dto.CreateClientRequestDto;
import com.credit.credit.dto.UpdateClientRequestDto;
import com.credit.credit.entity.Client;
import com.credit.credit.repository.ClientRepository;
import com.credit.credit.service.interfaces.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    @Override
    public Client createClient(CreateClientRequestDto request){
        clientRepository.findByPhoneNumber(request.phoneNumber())
                .ifPresent(client -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь уже существует");
                });
        Client client = new Client();

        client.setFullName(request.fullName());
        client.setFirstName(request.firstName());
        client.setLastName(request.lastName());
        client.setMiddleName(request.middleName());
        client.setPhoneNumber(request.phoneNumber());

        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Long id, UpdateClientRequestDto request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        boolean nameUpdated = Stream.of(
                        request.firstName(),
                        request.lastName(),
                        request.middleName())
                .anyMatch(Objects::nonNull);

        Optional.ofNullable(request.firstName()).ifPresent(client::setFirstName);
        Optional.ofNullable(request.lastName()).ifPresent(client::setLastName);
        Optional.ofNullable(request.middleName()).ifPresent(client::setMiddleName);
        Optional.ofNullable(request.phone()).ifPresent(client::setPhoneNumber);

        if (nameUpdated) {
            client.setFullName(String.format("%s %s %s",
                    client.getLastName(),
                    client.getFirstName(),
                    Optional.ofNullable(client.getMiddleName()).orElse("")).trim());
        }

        return clientRepository.save(client);
    }

    @Override
    public Client getClient(Long id){
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

}
