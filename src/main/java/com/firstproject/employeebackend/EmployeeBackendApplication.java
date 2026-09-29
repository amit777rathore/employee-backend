package com.firstproject.employeebackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@EnableCaching
@EnableAsync
@EnableScheduling
public class EmployeeBackendApplication {



    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        String jwtSecret = dotenv.get("JWT_SECRET");
        String jwtExpiration = dotenv.get("JWT_EXPIRATION");

        if (jwtSecret != null) {
            System.setProperty("JWT_SECRET", jwtSecret);
        }

        if (jwtExpiration != null) {
            System.setProperty("JWT_EXPIRATION", jwtExpiration);
        }

        String mailUsername = dotenv.get("MAIL_USERNAME");
        String mailPassword = dotenv.get("MAIL_PASSWORD");

        if (mailUsername != null) {
            System.setProperty("MAIL_USERNAME", mailUsername);
        }

        if (mailPassword != null) {
            System.setProperty("MAIL_PASSWORD", mailPassword);
        }


        SpringApplication.run(EmployeeBackendApplication.class, args);
    }

}
