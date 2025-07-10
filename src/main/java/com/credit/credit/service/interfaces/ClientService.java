package com.credit.credit.service.interfaces;

import com.credit.credit.dto.CreateClientRequestDto;
import com.credit.credit.dto.UpdateClientRequestDto;
import com.credit.credit.entity.Client;

public interface ClientService {
    Client createClient(CreateClientRequestDto request);

    Client updateClient(Long id, UpdateClientRequestDto request);

    Client getClient(Long id);
}
