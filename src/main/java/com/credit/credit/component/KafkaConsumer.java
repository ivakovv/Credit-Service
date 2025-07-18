package com.credit.credit.component;

import com.credit.credit.entity.Credit;
import com.credit.credit.enums.CreditStatus;
import com.credit.credit.service.CreditServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CreditServiceImpl creditService;

    @KafkaListener(topics = "new-credits", groupId = "credit-group", containerFactory = "longKafkaListenerContainerFactory")
    public void consumeNewCredit(Long creditId) {
        log.info("Received creditId: {}", creditId);
        creditService.changeCreditStatus(creditId, CreditStatus.IN_PROGRESS);
    }

    @KafkaListener(topics = "credits", groupId = "credit-group", containerFactory = "creditKafkaListenerContainerFactory")
    public void consumeCredit(Credit credit) {
        log.info("Credit id: {}, status: {}", credit.getId(), credit.getStatus());
    }
}