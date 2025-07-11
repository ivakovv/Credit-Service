package com.credit.credit.service;

import com.credit.credit.dto.credit.CreateCreditRequestDto;
import com.credit.credit.dto.credit.CreditResponseDto;
import com.credit.credit.entity.Client;
import com.credit.credit.entity.Credit;
import com.credit.credit.enums.CreditStatus;
import com.credit.credit.repository.ClientRepository;
import com.credit.credit.repository.CreditRepository;
import com.credit.credit.service.interfaces.CreditService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    private final ClientRepository clientRepository;

    private final ClientServiceImpl clientService;


    @Transactional
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

        Credit credit = new Credit();
        credit.setBusinessTransactionId(request.businessTransactionId());
        credit.setVersion(request.version());
        credit.setType(request.type());
        credit.setStatus(CreditStatus.NEW);
        credit.setRequestConditions(request.requestConditions());
        credit.setClient(client);

        Credit savedCredit = creditRepository.save(credit);

        client.getCredits().add(savedCredit);
        clientRepository.save(client);

        return CreditResponseDto.fromEntity(savedCredit);
    }

    @Override
    public CreditResponseDto getCredit(Long id){
        return CreditResponseDto.fromEntity(creditRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Кредит не найден")));
    }

}
