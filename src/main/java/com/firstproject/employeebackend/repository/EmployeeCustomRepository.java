package com.firstproject.employeebackend.repository;

import com.firstproject.employeebackend.entity.Employee;

import java.util.List;

public interface EmployeeCustomRepository {

    List<Employee> searchEmployeesCustom(
            String name,
            String email
    );
}

