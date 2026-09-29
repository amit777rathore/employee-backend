package com.firstproject.employeebackend.service;

import com.firstproject.employeebackend.dto.EmployeeRequestDTO;
import com.firstproject.employeebackend.entity.IdempotencyRequest;
import com.firstproject.employeebackend.repository.IdempotencyRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class IdempotencyService {

    private final IdempotencyRequestRepository repository;

    public IdempotencyService(IdempotencyRequestRepository repository) {
        this.repository = repository;
    }

    public Optional<IdempotencyRequest> findByKey(String key) {
        return repository.findByIdempotencyKey(key);
    }

    public IdempotencyRequest save(
            String key,
            String status,
            Long resourceId,
            String requestHash) {

        IdempotencyRequest request =
                new IdempotencyRequest(
                        key,
                        status,
                        LocalDateTime.now(),
                        resourceId,
                        requestHash
                );

        return repository.save(request);
    }

    public String generateRequestHash(EmployeeRequestDTO requestDTO) {

        String requestData =
                requestDTO.getName() + "|" +
                        requestDTO.getEmail() + "|" +
                        requestDTO.getDepartmentId();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes =
                    digest.digest(requestData.getBytes(StandardCharsets.UTF_8));

            StringBuilder hash = new StringBuilder();

            for (byte b : hashBytes) {
                hash.append(String.format("%02x", b));
            }

            return hash.toString();

        } catch (Exception e) {
            throw new RuntimeException("Unable to generate request hash", e);
        }
    }
}