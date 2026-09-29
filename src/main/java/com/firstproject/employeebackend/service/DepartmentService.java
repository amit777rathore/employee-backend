package com.firstproject.employeebackend.service;

import com.firstproject.employeebackend.dto.DepartmentRequestDTO;
import com.firstproject.employeebackend.entity.Department;
import com.firstproject.employeebackend.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository){this.departmentRepository= departmentRepository;}

    public Department saveDepartment(DepartmentRequestDTO departmentdto){


        Department department = new Department(
                departmentdto.getName()
        );

        return departmentRepository.save(department);
    }

    public List<Department> getDepartment(){
        return departmentRepository.findAll();
    }
}
