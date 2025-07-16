package com.credit.credit.mapper;

import com.credit.credit.dto.credit.CreateCreditRequestDto;
import com.credit.credit.entity.Client;
import com.credit.credit.entity.Credit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "businessTransactionId", ignore = true)
    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "client", source = "client")
    Credit toCredit(CreateCreditRequestDto request, Client client);
}
