package com.firstproject.employeebackend.controller;

import com.firstproject.employeebackend.service.AsyncService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class AsyncController {

    private final AsyncService asyncService;

    public AsyncController(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @GetMapping("/async-test")
    public String testAsync() {

        System.out.println("Controller thread: "
                + Thread.currentThread().getName());

        asyncService.runTask();

        return "Request completed";
    }

    @GetMapping("/async-message")
    public CompletableFuture<Void> getMessage() {

        System.out.println("Controller thread: "
                + Thread.currentThread().getName());

//        return asyncService.getMessage()
//                .thenApply(message -> message.toUpperCase());
        CompletableFuture<String> future =
                asyncService.getMessage();

        return  future.thenAccept(message ->
                System.out.println("Received message: " + message)
        );

    }

    @GetMapping("/async-error")
    public CompletableFuture<String> asyncError() {

        return asyncService.getError().thenApply(message -> {
                    System.out.println("Received message: " + message);
                    return message;
                }

        )
                .exceptionally(error -> {
                    System.out.println(
                            "Handled error: " + error.getMessage()
                    );

                    return "Something went wrong";
                });
    }
}