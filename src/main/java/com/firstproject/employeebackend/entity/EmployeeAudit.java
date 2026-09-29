package com.firstproject.employeebackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name="employee_audits")
public class EmployeeAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private String action;


    public EmployeeAudit() {
    }

    public EmployeeAudit(Long employeeId, String action) {
        this.employeeId = employeeId;
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
