package com.firstproject.employeebackend.repository;

import com.firstproject.employeebackend.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Employee> searchEmployeesCustom(
            String name,
            String email) {

        String jpql = """
                SELECT e
                FROM Employee e
                WHERE e.name = :name
                AND e.email = :email
                """;

        return entityManager
                .createQuery(jpql, Employee.class)
                .setParameter("name", name)
                .setParameter("email", email)
                .getResultList();
    }
}
