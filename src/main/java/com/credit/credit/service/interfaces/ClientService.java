package com.credit.credit.service.interfaces;

import com.credit.credit.dto.client.CreateClientRequestDto;
import com.credit.credit.dto.client.UpdateClientRequestDto;
import com.credit.credit.entity.Client;

public interface ClientService {
    Client createClient(CreateClientRequestDto request);

    Client updateClient(Long id, UpdateClientRequestDto request);

    Client getClient(Long id);
}
