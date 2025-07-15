package com.credit.credit.service;

import com.credit.credit.dto.client.CreateClientRequestDto;
import com.credit.credit.dto.client.UpdateClientRequestDto;
import com.credit.credit.entity.Client;
import com.credit.credit.exception.ClientAlreadyExistsException;
import com.credit.credit.exception.NotFoundException;
import com.credit.credit.mapper.ClientMapper;
import com.credit.credit.repository.ClientRepository;
import com.credit.credit.service.interfaces.ClientService;
import com.credit.credit.util.NameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    private final NameUtil nameUtil;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Client createClient(CreateClientRequestDto request){
        clientRepository.findByPhoneNumber(request.phoneNumber())
                .ifPresent(client -> {
                    throw new ClientAlreadyExistsException(request.phoneNumber());
                });

        Client client = clientMapper.toClient(request);

        return clientRepository.save(client);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Client updateClient(Long id, UpdateClientRequestDto request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client", id));

        nameUtil.updateNameFields(
                client,
                request.firstName(),
                request.lastName(),
                request.middleName()
        );

        Optional.ofNullable(request.phone()).ifPresent(client::setPhoneNumber);

        if (nameUtil.isNameUpdated(request.firstName(), request.lastName(), request.middleName())) {
            client.setFullName(nameUtil.generateFullName(
                    client.getLastName(),
                    client.getFirstName(),
                    client.getMiddleName()
            ));
        }

        return clientRepository.save(client);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public Client getClient(Long id){
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client", id));
    }

}
