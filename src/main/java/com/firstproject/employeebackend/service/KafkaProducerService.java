package com.firstproject.employeebackend.service;

import com.firstproject.employeebackend.dto.EmployeeCreatedEventDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "employee-created";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmployeeCreatedEvent(EmployeeCreatedEventDTO event) {

        kafkaTemplate.send(
                TOPIC,
                event.getEmployeeId().toString(),
                event
        );
    }
}