//package com.firstproject.employeebackend.controller;
//
//import com.firstproject.employeebackend.AppProperties;
//import com.firstproject.employeebackend.dto.EmployeeResponseDTO;
//import com.firstproject.employeebackend.service.EmployeeServices;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//
//@WebMvcTest(EmployeeController.class)
//class EmployeeControllerTest {
//
//    @MockitoBean
//    private EmployeeServices employeeServices;
//
//    @MockitoBean
//    private AppProperties appProperties;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void contextLoads() {
//    }
//
//    @Test
//    void shouldGetAllEmployees() throws Exception {
//
//        EmployeeResponseDTO employee = new EmployeeResponseDTO(
//                1L,
//                "Amit",
//                "amit@gmail.com"
//        );
//
//        when(employeeServices.getEmployee())
//                .thenReturn(List.of(employee));
//
//        mockMvc.perform(get("/employees"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[0].name").value("Amit"))
//                .andExpect(jsonPath("$[0].email").value("amit@gmail.com"));
//    }
//}