package org.demo.app.repo;


import org.assertj.core.api.Assertions;
import org.demo.app.model.EmployeeEntity;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Testcontainers
@SpringBootTest
class EmployeeRepoContainerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private EmployeeRepo employeeRepo;

    @Test
    @Order(1)
    void testSaveEmployee() {
        EmployeeEntity employee = EmployeeEntity.builder()
                .firstName("Ahmed")
                .lastName("Ali")
                .email("ahmed.ali@tesla.com")
                .salary(new BigDecimal(10000))
                .joinDate(new Date())
                .build();

        EmployeeEntity savedEmployee = employeeRepo.save(employee);
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("ahmed.ali@tesla.com");

    }

    @Test
    @Order(2)
    void testFindByEmail() {
        Optional<EmployeeEntity> employeeEntity = employeeRepo.findByEmail("ahmed.ali@tesla.com");
        Assertions.assertThat(employeeEntity.isPresent()).isTrue();
    }

}
