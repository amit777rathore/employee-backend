//package com.firstproject.employeebackend.service;
//
//import com.firstproject.employeebackend.dto.EmployeeResponseDTO;
//import com.firstproject.employeebackend.entity.Employee;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import com.firstproject.employeebackend.repository.EmployeeRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class EmployeeServiceComponentTest {
//
//    @Autowired
//    private EmployeeServices employeeServices;
//
////    @MockitoBean
////    private EmployeeRepository employeeRepository;
//
//
//    @Test
//    void contextLoads() {
//        assertNotNull(employeeServices);
//    }
//
//    @Test
//    void shouldReturnEmployeesUsingSpringContext(){
//        List<EmployeeResponseDTO> result =
//                employeeServices.getEmployee();
//
//        assertNotNull(result);
//    }
//
////    @Test
////    void shouldReturnEmployeesUsingSpringContext() {
////
////        Employee employee = new Employee();
////        employee.setId(1L);
////        employee.setName("Amit");
////        employee.setEmail("amit@gmail.com");
////
////        when(employeeRepository.findAll())
////                .thenReturn(List.of(employee));
////
////        List<EmployeeResponseDTO> result =
////                employeeServices.getEmployee();
////
////        assertEquals(1, result.size());
////        assertEquals("Amit", result.get(0).getName());
////    }
//}
