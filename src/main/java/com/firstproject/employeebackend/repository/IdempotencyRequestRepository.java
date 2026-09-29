package com.firstproject.employeebackend.repository;

import com.firstproject.employeebackend.entity.IdempotencyRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRequestRepository
        extends JpaRepository<IdempotencyRequest, Long> {

    Optional<IdempotencyRequest> findByIdempotencyKey(String idempotencyKey);
}