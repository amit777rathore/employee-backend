package com.firstproject.employeebackend.specification;

import com.firstproject.employeebackend.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> hasName(String name) {
        return (root, query, criteriaBuilder) -> {

            if (name == null || name.isBlank()) {
                return null;
            }

            return criteriaBuilder.equal(root.get("name"), name);
        };
    }

    public static Specification<Employee> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {

            if (email == null || email.isBlank()) {
                return null;
            }

            return criteriaBuilder.equal(root.get("email"), email);
        };
    }
}