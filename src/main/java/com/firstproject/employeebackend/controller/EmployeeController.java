package com.firstproject.employeebackend.controller;

import com.firstproject.employeebackend.AppProperties;
import com.firstproject.employeebackend.dto.ApiResponse;
import com.firstproject.employeebackend.dto.EmployeeRequestDTO;
import com.firstproject.employeebackend.dto.EmployeeResponseDTO;
import com.firstproject.employeebackend.dto.PaginationResponse;
import com.firstproject.employeebackend.entity.Employee;
import com.firstproject.employeebackend.entity.IdempotencyRequest;
import com.firstproject.employeebackend.service.EmployeeAsyncService;
import com.firstproject.employeebackend.service.EmployeeServices;
import com.firstproject.employeebackend.service.IdempotencyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("employees")
public class EmployeeController {

    private final AppProperties appProperties;
    private final EmployeeServices employeeServices;
    private final IdempotencyService idempotencyService;
    private final EmployeeAsyncService employeeAsyncService;

    private static final Logger log =
            LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeServices employeeServices, AppProperties appProperties,IdempotencyService idempotencyService
    , EmployeeAsyncService employeeAsyncService) {
        this.employeeServices = employeeServices;
        this.appProperties = appProperties;
        this.idempotencyService = idempotencyService;
        this.employeeAsyncService = employeeAsyncService;
    }



//    @Value("${app.company-name}")
//    private String companyName;
//
//    @GetMapping("/config")
//    public String getConfig() {
//        return companyName;
//    }


    @GetMapping("/whoami")
    public String whoAmI(Authentication authentication) {
        System.out.println("Principal: " + authentication.getPrincipal());
        System.out.println("Principal class: " +
                authentication.getPrincipal().getClass());

        return authentication.getName() + " -> " + authentication.getAuthorities();
    }

    @GetMapping("/config")
    public String getConfig() {
        return appProperties.getCompanyName();
    }

    @PostMapping
    public ResponseEntity <EmployeeResponseDTO >saveEmployee(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid  @RequestBody EmployeeRequestDTO employee) {

        String requestHash =
                idempotencyService.generateRequestHash(employee);

        Optional<IdempotencyRequest> existingRequest =
                idempotencyService.findByKey(idempotencyKey);

//        if (existingRequest.isPresent()) {
//            throw new IllegalArgumentException(
//                    "Request already processed for this Idempotency-Key"
//            );
//        }

        if (existingRequest.isPresent()) {

            IdempotencyRequest existing =
                    existingRequest.get();

            if (existing.getRequestHash() != null &&!existing.getRequestHash().equals(requestHash)) {
                throw new IllegalArgumentException(
                        "Same Idempotency-Key cannot be used with different request data"
                );
            }

            Long employeeId = existing.getResourceId();

            EmployeeResponseDTO existingEmployee =
                    employeeServices.getEmployeeById(employeeId);

            return ResponseEntity.ok(existingEmployee);
        }

        EmployeeResponseDTO res = employeeServices.saveEmployee(employee);

        idempotencyService.save(
                idempotencyKey,
                "COMPLETED",
                res.getId(),
                requestHash
        );
        System.out.println(
                "Background thread: " +
                        Thread.currentThread().getName()
        );
        employeeAsyncService.employeeCreated(res.getId());


        URI location = URI.create(
                "/employees/" + res.getId()
        );

        return ResponseEntity.created(location)
                .body(res);

       // return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping
    public ApiResponse<List<EmployeeResponseDTO>> getEmployee()
    {
        log.info("Fetching all employees");
        List<EmployeeResponseDTO> employees= employeeServices.getEmployee();

        return new ApiResponse<>(
                true,
                "Employees fetched successfully",
                employees
        );

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.debug("Logging debug: {}",id);
        employeeServices.deleteEmployee(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public EmployeeResponseDTO updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO employee) {
        return employeeServices.updateEmployee(id, employee);

    }

    @PatchMapping("/{id}")
    public Employee patchEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeServices.patchEmployee(id, employee);

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<EmployeeResponseDTO>>  getEmployeeBYName(@PathVariable String name){
     List<EmployeeResponseDTO> res =  employeeServices.getEmployeeByName(name);

     return ResponseEntity.ok( res);
    }

    @GetMapping("/depart/{id}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeeById(@PathVariable Long id){
        List<EmployeeResponseDTO> res = employeeServices.getEmployeeByDepartId(id);
        return ResponseEntity.ok( res);

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByDepartId(@PathVariable Long id){
        EmployeeResponseDTO res = employeeServices.getEmployeeById(id);
        return ResponseEntity.ok( res);

    }

    @GetMapping("/query/name/{name}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByNameUsingQuery(
            @PathVariable String name) {

        return ResponseEntity.ok(
                employeeServices.getEmployeesByNameUsingQuery(name)
        );
    }

    @GetMapping("/department/{departmentId}/name/{name}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByDepartmentAndName(
            @PathVariable Long departmentId,
            @PathVariable String name) {

        return ResponseEntity.ok(
                employeeServices.getEmployeeByDepartnameAndName(
                        departmentId,
                        name
                )
        );
    }

    @GetMapping("/pagination")
    public ResponseEntity<ApiResponse<PaginationResponse<EmployeeResponseDTO>>> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        PaginationResponse<EmployeeResponseDTO> data =
                employeeServices.getEmployees(page, size, sortBy, direction);

        ApiResponse<PaginationResponse<EmployeeResponseDTO>> response =
                new ApiResponse<>(
                        true,
                        "Employees fetched successfully",
                        data
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<EmployeeResponseDTO>> searchByName(
            @PathVariable String name) {

        return ResponseEntity.ok(
                employeeServices.searchByName(name)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponseDTO>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {

        return ResponseEntity.ok(
                employeeServices.searchEmployees(name, email)
        );
    }

    @GetMapping("/searchEmployee")
    public ResponseEntity<Page<EmployeeResponseDTO>> searchEmployeeDynamically(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                employeeServices.searchEmployeeDynamically(
                        page, size, name, email, sortBy, direction
                )
        );
    }

    @GetMapping("/department/name/{departmentName}/employee/{name}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByDepartmentNameAndName(
            @PathVariable String departmentName,
            @PathVariable String name) {

        return ResponseEntity.ok(
                employeeServices.getEmployeesByDepartmentNameAndName(
                        departmentName,
                        name
                )
        );
    }

    @GetMapping("/with-department/{name}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesWithDepartment(
            @PathVariable String name) {

        return ResponseEntity.ok(
                employeeServices.getEmployeesWithDepartment(name)
        );
    }

    @GetMapping("/serachEmployee/{name}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeeWithNative(
            @PathVariable String name
    ){
        return ResponseEntity.ok(
                employeeServices.searchEmployeeWithNative(name)
        );
    }

    @GetMapping("/custom-search")
    public ResponseEntity<List<EmployeeResponseDTO>> searchEmployeesCustom(
            @RequestParam String name,
            @RequestParam String email) {

        return ResponseEntity.ok(
                employeeServices.searchEmployeesCustom(name, email)
        );
    }

}
