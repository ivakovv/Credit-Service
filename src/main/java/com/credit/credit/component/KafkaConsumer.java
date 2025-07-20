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

    @KafkaListener(topics = "${spring.kafka.consumer.topics.new-credits}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "longKafkaListenerContainerFactory")
    public void consumeNewCredit(Long creditId) {
        log.info("Received creditId: {}", creditId);
        creditService.changeCreditStatus(creditId, CreditStatus.IN_PROGRESS);
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topics.credits}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "creditKafkaListenerContainerFactory")
    public void consumerCredit(Credit credit) {
        log.info("Credit id: {}, status: {}", credit.getId(), credit.getStatus());
        creditService.finalizeCredit(credit);
        log.info("Credit {} moved to final status: {}", credit.getId(), credit.getStatus());
    }
}