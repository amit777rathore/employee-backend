package com.firstproject.employeebackend.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    @Async
    public void runTask() {

        System.out.println("Async task started: "
                + Thread.currentThread().getName());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Async task completed: "
                + Thread.currentThread().getName());
    }

    @Async
    public CompletableFuture<String> getMessage() {

        System.out.println("Async thread: "
                + Thread.currentThread().getName());

        return CompletableFuture.completedFuture("Hello from async");
    }

    @Async
    public CompletableFuture<String> getError() {

        System.out.println("Async error task started");

      //  throw new RuntimeException("Async task failed");

        System.out.println("Async error task complete");

        return CompletableFuture.completedFuture("Hello from async");


    }
}