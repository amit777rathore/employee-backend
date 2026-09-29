package com.firstproject.employeebackend.controller;

import com.firstproject.employeebackend.dto.DepartmentRequestDTO;
import com.firstproject.employeebackend.entity.Department;
import com.firstproject.employeebackend.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    public DepartmentController(DepartmentService departmentService){this.departmentService = departmentService;}



    @PostMapping
    public ResponseEntity<Department>saveDepartment(@RequestBody DepartmentRequestDTO department){
        Department res = departmentService.saveDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping
    public List<Department> getDepartment(){
    return departmentService.getDepartment();
    }



}
