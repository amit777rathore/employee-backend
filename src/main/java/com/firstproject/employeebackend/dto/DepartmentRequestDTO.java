package com.firstproject.employeebackend.dto;

public class DepartmentRequestDTO {

    private String name;

    public DepartmentRequestDTO() {
    }

    public DepartmentRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
