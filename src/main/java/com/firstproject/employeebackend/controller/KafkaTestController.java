package com.firstproject.employeebackend.controller;

import com.firstproject.employeebackend.dto.EmployeeCreatedEventDTO;
import com.firstproject.employeebackend.service.KafkaProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaTestController {

    private final KafkaProducerService kafkaProducerService;

    public KafkaTestController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("/kafka-test")
    public String kafkaTest() {

        EmployeeCreatedEventDTO event = new EmployeeCreatedEventDTO(
                101L,
                "Rahul",
                "rahul@gmail.com"
        );

        kafkaProducerService.sendEmployeeCreatedEvent(event);

        return "Kafka message sent";
    }
}