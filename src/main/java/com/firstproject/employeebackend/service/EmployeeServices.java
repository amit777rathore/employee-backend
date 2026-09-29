package com.firstproject.employeebackend.service;

import com.firstproject.employeebackend.dto.EmployeeCreatedEventDTO;
import com.firstproject.employeebackend.dto.EmployeeRequestDTO;
import com.firstproject.employeebackend.dto.EmployeeResponseDTO;
import com.firstproject.employeebackend.dto.PaginationResponse;
import com.firstproject.employeebackend.entity.Department;
import com.firstproject.employeebackend.entity.Employee;
import com.firstproject.employeebackend.entity.EmployeeAudit;
import com.firstproject.employeebackend.exception.EmployeeNotFoundException;
import com.firstproject.employeebackend.repository.DepartmentRepository;
import com.firstproject.employeebackend.repository.EmployeeAuditRepository;
import com.firstproject.employeebackend.repository.EmployeeRepository;
import com.firstproject.employeebackend.specification.EmployeeSpecification;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EmployeeServices {
    private final EmployeeRepository employeeRepository;
    private final EmployeeAuditRepository employeeAuditRepository;
    private final DepartmentRepository departmentRepository;
    private final KafkaProducerService kafkaProducerService;

    public EmployeeServices(EmployeeRepository employeeRepository, EmployeeAuditRepository employeeAuditRepository, DepartmentRepository departmentRepository,KafkaProducerService kafkaProducerService){
        this.employeeRepository=employeeRepository;
        this.employeeAuditRepository= employeeAuditRepository;
        this.departmentRepository = departmentRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Transactional
    public EmployeeResponseDTO saveEmployee(EmployeeRequestDTO requestDTO){

        Employee employee = new Employee();

        employee.setName(requestDTO.getName());
        employee.setEmail(requestDTO.getEmail());


        Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        employee.setDepartment(department);



        Employee savedEmployee = employeeRepository.save(employee);

        EmployeeAudit audit  = new EmployeeAudit(
                savedEmployee.getId(),
                "Employee Created"
        );

        employeeAuditRepository.save(audit);
        //throw new RuntimeException("Testing transaction rollback");

        EmployeeCreatedEventDTO event = new EmployeeCreatedEventDTO(
                savedEmployee.getId(),
                savedEmployee.getName(),
                savedEmployee.getEmail()
        );

        kafkaProducerService.sendEmployeeCreatedEvent(event);


       return new EmployeeResponseDTO(
               savedEmployee.getId(),
               savedEmployee.getName(),
               savedEmployee.getEmail(),
               savedEmployee.getDepartment().getId()
       );


    }

    public List<EmployeeResponseDTO> getEmployee(){
        List<Employee> employee = employeeRepository.findAll();

        return employee.stream().map(employee1 -> new EmployeeResponseDTO(
                employee1.getId(),
                employee1.getName(),
                employee1.getEmail(),
                employee1.getDepartment() != null
                        ? employee1.getDepartment().getId()
                        : null
        )).toList();


        //return employeeRepository.findAll();
    }

    @CacheEvict(value = "employees", key = "#id")
   @PreAuthorize("hasRole('ADMIN')")
   // @PreAuthorize("authentication.name == 'admin'")
    public void deleteEmployee(Long id){

        Employee exisingemployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"+ id));


        employeeRepository.deleteById(id);
    }

    @CachePut(value = "employees", key = "#id")
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO employee){
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"+id));

            existingEmployee.setName(employee.getName());



            existingEmployee.setEmail(employee.getEmail());

            Employee updatedEmployeeObj = employeeRepository.save(existingEmployee);

            EmployeeResponseDTO updatedEmployee = new EmployeeResponseDTO(
                    updatedEmployeeObj.getId(),
                    updatedEmployeeObj.getName(),
                    updatedEmployeeObj.getEmail(),
                    updatedEmployeeObj.getDepartment() != null
                            ? updatedEmployeeObj.getDepartment().getId()
                            : null
            );


        //return

        return updatedEmployee;


    }

    @CacheEvict(value = "employees", key = "#id")
    public Employee patchEmployee(Long id, Employee employee) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"+ id ));

        if (employee.getName() != null) {
            existingEmployee.setName(employee.getName());
        }

        if (employee.getEmail() != null) {
            existingEmployee.setEmail(employee.getEmail());
        }

        return employeeRepository.save(existingEmployee);
    }


    public List<EmployeeResponseDTO> getEmployeeByName(String name){
        List<Employee> employees = employeeRepository.findByNameContainingIgnoreCase(name);
        //console.log("employees",employees);

        return employees.stream()
                .map(employee -> new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getDepartment() != null
                                ? employee.getDepartment().getId()
                                : null

                ))
                .toList();
    }

    public List<EmployeeResponseDTO> getEmployeeByDepartId(Long id){
        List<Employee> employees = employeeRepository.findByDepartmentId(id);
        return employees.stream()
                .map(employee -> new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getDepartment() != null
                                ? employee.getDepartment().getId()
                                : null

                ))
                .toList();
    }

    public List<EmployeeResponseDTO> getEmployeesByNameUsingQuery(String name) {

        List<Employee> employees =
                employeeRepository.findEmployeesByName(name);

        return employees.stream()
                .map(employee -> new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getDepartment() != null
                                ? employee.getDepartment().getId()
                                : null
                ))
                .toList();
    }

    public List<EmployeeResponseDTO> getEmployeeByDepartnameAndName(Long id, String name){
      List<Employee> employees = employeeRepository.findByDepartmentAndName(id,name);

        return employees.stream()
                .map(employee -> new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getDepartment() != null
                                ? employee.getDepartment().getId()
                                : null
                ))
                .toList();
    }

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "name", "email");


    public PaginationResponse<EmployeeResponseDTO> getEmployees(int page, int size, String sortBy,
                                                                String direction){


        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy
            );
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Sort direction must be either asc or desc"
            );
        }


        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employees =
                employeeRepository.findAll(pageable);

         Page<EmployeeResponseDTO> employeePage =  employees.map(employee ->
                new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getDepartment() != null
                                ? employee.getDepartment().getId()
                                : null
                )
        );


        return new PaginationResponse<>(
                employeePage.getContent(),
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isFirst(),
                employeePage.isLast()
        );
    }

    public List<EmployeeResponseDTO> searchByName(String name) {

        Specification<Employee> specification =
                EmployeeSpecification.hasName(name);

        List<Employee> employees =
                employeeRepository.findAll(specification);

        return employees.stream()
                .map(employee -> new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getDepartment() != null
                                ? employee.getDepartment().getId()
                                : null
                ))
                .toList();
    }

    public List<EmployeeResponseDTO> searchEmployees(
            String name,
            String email) {

        Specification<Employee> specification =
                Specification.where(EmployeeSpecification.hasName(name))
                        .and(EmployeeSpecification.hasEmail(email));

        List<Employee> employees =
                employeeRepository.findAll(specification);

        return employees.stream()
                .map(employee -> new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getDepartment() != null
                                ? employee.getDepartment().getId()
                                : null
                ))
                .toList();
    }

    public Page<EmployeeResponseDTO> searchEmployeeDynamically(
            int page,
            int size,
            String name,
            String email,
            String sortBy,
            String direction) {

        // 1. Dynamic filtering
        Specification<Employee> specification =
                Specification.where(EmployeeSpecification.hasName(name))
                        .and(EmployeeSpecification.hasEmail(email));

        // 2. Dynamic sorting
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // 3. Pagination + sorting
        Pageable pageable = PageRequest.of(page, size, sort);

        // 4. Filtering + pagination + sorting together
        Page<Employee> employees =
                employeeRepository.findAll(specification, pageable);

        // 5. Entity → DTO
        return employees.map(employee ->
                new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getDepartment() != null
                                ? employee.getDepartment().getId()
                                : null
                )
        );
    }

  public List<EmployeeResponseDTO> getEmployeesByDepartmentNameAndName(String dName, String name){
        List<Employee> emp = employeeRepository.findByDepartmentNameAndName(dName,name);

        return emp.stream().map(employee -> new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment() != null
                        ? employee.getDepartment().getId()
                        : null
        )).toList();
  }

    public List<EmployeeResponseDTO> getEmployeesWithDepartment(String name) {

        List<Employee> employees =
                employeeRepository.findEmployeesWithDepartment(name);

        return employees.stream()
                .map(employee -> new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getDepartment() != null
                                ? employee.getDepartment().getId()
                                : null
                ))
                .toList();
    }

    public List<EmployeeResponseDTO> searchEmployeeWithNative(String name){
        List<Employee> emp = employeeRepository.findEmployeesByEmailNative(name);

        return emp.stream().map(employee -> new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment() != null
                        ? employee.getDepartment().getId()
                        : null
        )).toList();
    }

    public List<EmployeeResponseDTO> searchEmployeesCustom(
            String name,
            String email) {

        List<Employee> employees =
                employeeRepository.searchEmployeesCustom(name, email);

        return employees.stream()
                .map(employee -> new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getDepartment() != null
                                ? employee.getDepartment().getId()
                                : null
                ))
                .toList();
    }

    @Cacheable(value = "employees", key = "#id")
    public EmployeeResponseDTO getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found" + id)
                );

        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment() != null
                        ? employee.getDepartment().getId()
                        : null
        );
    }



}
