package com.credit.credit.service;

import com.credit.credit.dto.credit.CreateCreditRequestDto;
import com.credit.credit.dto.credit.CreditResponseDto;
import com.credit.credit.entity.Client;
import com.credit.credit.entity.Credit;
import com.credit.credit.enums.CreditStatus;
import com.credit.credit.exception.NotFoundException;
import com.credit.credit.mapper.CreditMapper;
import com.credit.credit.repository.ClientRepository;
import com.credit.credit.repository.CreditRepository;
import com.credit.credit.service.interfaces.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
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

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.producer.topics.credits}")
    private String creditsTopic;

    @Value("${spring.kafka.producer.topics.new-credits}")
    private String newCreditsTopic;


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

        sendCreditToKafka(newCreditsTopic, credit.getId());

        return CreditResponseDto.fromEntity(savedCredit);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public CreditResponseDto getCredit(Long id){
        return CreditResponseDto.fromEntity(creditRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Credit", id)));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void changeCreditStatus(Long id, CreditStatus status) {
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Credit", id));
        credit.setStatus(status);
        creditRepository.save(credit);
        sendCreditToKafka(creditsTopic, credit);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void finalizeCredit(Credit credit) {
        credit.setStatus(CreditStatus.CLOSED);
        creditRepository.save(credit);
    }

    public void sendCreditToKafka(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
}
