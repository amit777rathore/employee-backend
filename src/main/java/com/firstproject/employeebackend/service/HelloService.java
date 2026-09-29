package com.firstproject.employeebackend.service;

import com.firstproject.employeebackend.entity.Employee;
import com.firstproject.employeebackend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class HelloService {



     private final EmployeeRepository employeeRepository;

     public HelloService(EmployeeRepository employeeRepository){
         this.employeeRepository= employeeRepository;
     }

     public List<Employee> getEmployee(){
       // return  employeeRepository.getEmployee();
        return  employeeRepository.findAll();
     }

      Employee empObj= new Employee(1L,"amit","amit@hmail");

        public String getMessage(){
           // employeeRepository.save({id:"1",name:"amit",email:"amit@gmail.com")
            employeeRepository.save(empObj);
         return "Hello from service. data saved.";
        }

}
