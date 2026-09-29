package com.firstproject.employeebackend.controller;

import com.firstproject.employeebackend.entity.Employee;
import com.firstproject.employeebackend.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService){
        this.helloService = helloService;
    }

//    @GetMapping("/employees")
//    public List<Employee> employees(){
//        return helloService.getEmployee();
//    }



    @GetMapping("/hello")
    public String sayHello() {
        //return "Hello Amit! Welcome to Spring Boot";

        return helloService.getMessage();
    }
}