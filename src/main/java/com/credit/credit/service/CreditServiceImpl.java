package com.credit.credit.service;

import com.credit.credit.dto.credit.CreateCreditRequestDto;
import com.credit.credit.dto.credit.CreditResponseDto;
import com.credit.credit.entity.Client;
import com.credit.credit.entity.Credit;
import com.credit.credit.exception.NotFoundException;
import com.credit.credit.mapper.CreditMapper;
import com.credit.credit.repository.ClientRepository;
import com.credit.credit.repository.CreditRepository;
import com.credit.credit.service.interfaces.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    private final ClientRepository clientRepository;

    private final ClientServiceImpl clientService;

    private final CreditMapper creditMapper;


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public CreditResponseDto createCredit(CreateCreditRequestDto request) {
        Client client = clientService.getClient(request.clientId());

        Optional<Credit> existingCredit = creditRepository.findByClientIdAndRequestConditionsAmountAndRequestConditionsTerm(
                client.getId(),
                request.requestConditions().getAmount(),
                request.requestConditions().getTerm()
        );

        if (existingCredit.isPresent()) {
            return CreditResponseDto.fromEntity(existingCredit.get());
        }

        Credit credit = creditMapper.toCredit(request, client);

        Credit savedCredit = creditRepository.save(credit);

        if (client.getCredits() == null) {
            client.setCredits(new ArrayList<>());
        }
        client.getCredits().add(savedCredit);

        clientRepository.save(client);

        return CreditResponseDto.fromEntity(savedCredit);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public CreditResponseDto getCredit(Long id){
        return CreditResponseDto.fromEntity(creditRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Credit", id)));
    }

}
