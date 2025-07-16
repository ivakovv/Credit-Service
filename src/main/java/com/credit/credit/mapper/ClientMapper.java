package com.credit.credit.mapper;

import com.credit.credit.dto.client.CreateClientRequestDto;
import com.credit.credit.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toClient(CreateClientRequestDto request);
}
