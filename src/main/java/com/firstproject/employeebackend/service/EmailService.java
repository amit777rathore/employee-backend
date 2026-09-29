package com.firstproject.employeebackend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmployeeWelcomeEmail(
            String to,
            String employeeName
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("vikasrathore1607@gmail.com");
        message.setTo(to);
        message.setSubject("Welcome to Employee System");
        message.setText(
                "Hello " + employeeName + ",\n\n"
                        + "Your employee account has been created successfully.\n\n"
                        + "Welcome!"
        );

        mailSender.send(message);
    }
}