package com.firstproject.employeebackend.service;

import com.firstproject.employeebackend.dto.EmployeeCreatedEventDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmployeeKafkaConsumer {

    private final EmailService emailService;

    public EmployeeKafkaConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "employee-created",
            groupId = "employee-notification-group"
    )
    public void consumeEmployeeCreatedEvent(EmployeeCreatedEventDTO event) {

        System.out.println(
                "Kafka Event Received: Employee ID = "
                        + event.getEmployeeId()
                        + ", Name = "
                        + event.getName()
                        + ", Email = "
                        + event.getEmail()
        );

        emailService.sendEmployeeWelcomeEmail(
                event.getEmail(),
                event.getName()
        );
    }
}