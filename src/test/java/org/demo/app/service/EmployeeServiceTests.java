package org.demo.app.service;


import org.demo.app.dto.EmployeeDto;
import org.demo.app.model.EmployeeEntity;
import org.demo.app.repo.EmployeeRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeServiceTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private EmployeeService employeeService;

    @Test
    @Order(1)
    @DisplayName("JUnit test to create employee")
    void testCreateEmployee() {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("Ahmed")
                .lastName("Ali")
                .email("ahmed.ali@gmail.com")
                .salary(new BigDecimal(15000))
                .joinDate(new Date())
                .build();
        EmployeeDto savedEmployee = employeeService.create(employeeDto);
        assertNotNull(savedEmployee);
        assertEquals("ahmed.ali@gmail.com", savedEmployee.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("JUnit test to update employee")
    void testUpdateEmployee() {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .firstName("Ahmed")
                .lastName("Ali")
                .email("ahmed.ali@gmail.com")
                .salary(new BigDecimal(20000))
                .joinDate(new Date())
                .build();
        EmployeeDto savedEmployee = employeeService.update(1L, employeeDto);
        assertNotNull(savedEmployee);
        assertEquals("ahmed.ali@gmail.com", savedEmployee.getEmail());
        assertEquals(new BigDecimal(20000), savedEmployee.getSalary());
    }

    @Test
    @Order(3)
    @DisplayName("JUnit test to find employee by ID")
    void testFindEmployeeById() {
        EmployeeDto employeeDto = employeeService.findById(1L);
        assertNotNull(employeeDto);
        assertEquals("ahmed.ali@gmail.com", employeeDto.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("JUnit test to find employee by email")
    void testFindEmployeeByEmail() {
        EmployeeDto employeeDto = employeeService.findByEmail("ahmed.ali@gmail.com");
        assertNotNull(employeeDto);
        assertEquals("ahmed.ali@gmail.com", employeeDto.getEmail());
    }

    @Test
    @Order(5)
    @DisplayName("JUnit test to find all employees")
    void testFindAllEmployees() {
        List<EmployeeDto> list = employeeService.findList();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    @Order(6)
    @DisplayName("JUnit test to delete a employee")
    void testDeleteEmployee() {
        employeeService.delete(1L);
        Optional<EmployeeEntity> employee = employeeRepo.findById(1L);
        assertFalse(employee.isPresent());
    }

    @Test
    @Order(7)
    @DisplayName("JUnit test to create dummy employees list")
    void testCreateEmployeeList() {
        List<EmployeeDto> list = employeeService.createRandomList(5);
        assertNotNull(list);
        assertEquals(5, list.size());
    }

    @Test
    @Order(8)
    @DisplayName("JUnit test to count employees")
    void testCountEmployees() {
        Long count = employeeService.count();
        assertNotNull(count);
        assertEquals(5, count);
    }

}
