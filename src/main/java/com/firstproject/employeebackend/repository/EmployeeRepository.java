package com.firstproject.employeebackend.repository;

import com.firstproject.employeebackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>, JpaSpecificationExecutor<Employee>, EmployeeCustomRepository {
    List<Employee> findByNameContainingIgnoreCase(String name);

    @Query("SELECT e FROM Employee e WHERE e.name = :name")
    List<Employee> findEmployeesByName(@Param("name") String name);

    List<Employee> findByEmail(String email);

    List<Employee> findByDepartmentId(Long departmentId);

    @Query("""
       SELECT e
       FROM Employee e
       WHERE e.department.id = :departmentId
       AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))
       """)
    List<Employee> findByDepartmentAndName(
            @Param("departmentId") Long departmentId,
            @Param("name") String name);



    @Query("""
    SELECT e
    FROM Employee e
    JOIN e.department d
    WHERE d.name = :departmentName
    AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))
    """)
    List<Employee> findByDepartmentNameAndName(
            @Param("departmentName") String departmentName,
            @Param("name") String name);


    @Query("""
    SELECT e
    FROM Employee e
    JOIN FETCH e.department
    WHERE e.name = :name
    """)
    List<Employee> findEmployeesWithDepartment(
            @Param("name") String name);


    @Query(value = """
    SELECT *
    FROM employees
    WHERE email = :email
    """, nativeQuery = true)
    List<Employee> findEmployeesByEmailNative(
            @Param("email") String email);


}



