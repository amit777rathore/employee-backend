package com.firstproject.employeebackend.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ScheduledService {

    private final EmployeeAsyncService employeeAsyncService;

    public ScheduledService(EmployeeAsyncService employeeAsyncService) {
        this.employeeAsyncService = employeeAsyncService;
    }

//    @Scheduled(fixedRate = 10000)
//    public void runScheduledTask() {
//
//        System.out.println(
//                "Scheduler thread employee: "
//                        + Thread.currentThread().getName()
//        );
//
//        employeeAsyncService.runBackgroundTask();
//    }

//    @Scheduled(fixedDelay = 500000)
//    public void runTask() {
//
//        System.out.println(
//                "Task started fixed delay: f"
//                        + Thread.currentThread().getName()
//        );
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        System.out.println("Task completed fixed delay");
//    }
//
//    @Scheduled(cron = "0 15 10 5 * *")
//    public void runTask1() {
//
//        System.out.println(
//                "Cron task running: "
//                        + Thread.currentThread().getName()
//        );
//    }

//    @Scheduled(fixedRate = 5000)
//    public void runScheduledTask() {
//
//        System.out.println(
//                "Task started: "
//                        + System.currentTimeMillis()
//                        + " | "
//                        + Thread.currentThread().getName()
//        );
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        System.out.println(
//                "Task completed: "
//                        + System.currentTimeMillis()
//        );
//    }

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Scheduled(fixedRate = 5000)
    public void runScheduledTask1() {

//        if (!running.compareAndSet(false, true)) {
//            System.out.println("Task already running. Skipping...");
//            return;
//        }
//
//        System.out.println(
//                "Scheduler triggered: "
//                        + Thread.currentThread().getName()
//        );
//
//        employeeAsyncService.runBackgroundTask1(running);
    }

}