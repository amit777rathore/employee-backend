package com.firstproject.employeebackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name="departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;


    public Department() {
    }


    public String getName() {
        return name;
    }

    public Department(String name) {
        this.name = name;

    }



    public void setName(String name) {
        this.name = name;
    }


    public long getId() {
        return id;
    }



    public void setId(long id) {
        this.id = id;
    }
}
