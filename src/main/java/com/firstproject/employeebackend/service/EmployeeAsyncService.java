package com.firstproject.employeebackend.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class EmployeeAsyncService {

    @Async
    public void employeeCreated(Long employeeId) {
        System.out.println(
                "Background thread: " +
                        Thread.currentThread().getName()
        );

        System.out.println(
                "Background task started for employee: " + employeeId
        );

        System.out.println(
                "Audit/notification processing for employee: " + employeeId
        );

        System.out.println(
                "Background task completed for employee: " + employeeId
        );
        throw new RuntimeException("Async task failed");
    }

    @Async
    public void runBackgroundTask() {

        System.out.println(
                "Async thread: "
                        + Thread.currentThread().getName()
        );

        System.out.println("Background processing started");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Background processing completed");
    }

    @Async
    public void runBackgroundTask1(AtomicBoolean running) {

        try {

            System.out.println(
                    "Async thread: "
                            + Thread.currentThread().getName()
            );

            System.out.println("Background processing started");

            Thread.sleep(10000);

            System.out.println("Background processing completed");

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

        } finally {

            running.set(false);
        }
    }
}