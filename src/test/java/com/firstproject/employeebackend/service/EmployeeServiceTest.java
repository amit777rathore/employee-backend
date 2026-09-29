package com.firstproject.employeebackend.service;
import com.firstproject.employeebackend.dto.EmployeeResponseDTO;
import com.firstproject.employeebackend.repository.DepartmentRepository;
import com.firstproject.employeebackend.repository.EmployeeAuditRepository;
import com.firstproject.employeebackend.repository.EmployeeRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import com.firstproject.employeebackend.entity.Employee;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeAuditRepository employeeAuditRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServices employeeServices;


    @Test
    void shouldReturnEmployees() {

        // Arrange
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Amit");
        employee.setEmail("amit@gmail.com");

        when(employeeRepository.findAll())
                .thenReturn(List.of(employee));

        List<EmployeeResponseDTO> result = employeeServices.getEmployee();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Amit", result.get(0).getName());
        assertEquals("amit@gmail.com", result.get(0).getEmail());

        verify(employeeRepository).findAll();

    }

    @Test
    void shouldReturnEmptyListWhenNoEmployees() {

        // Arrange
        when(employeeRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<EmployeeResponseDTO> result =
                employeeServices.getEmployee();

        // Assert
        assertEquals(0, result.size());

        // Verify
        verify(employeeRepository).findAll();
    }


}