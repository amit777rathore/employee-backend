package com.firstproject.employeebackend.repository;

import com.firstproject.employeebackend.entity.EmployeeAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  EmployeeAuditRepository extends JpaRepository<EmployeeAudit,Long> {

}
