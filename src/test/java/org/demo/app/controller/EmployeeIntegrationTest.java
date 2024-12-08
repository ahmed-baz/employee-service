package org.demo.app.controller;


import org.demo.app.dto.EmployeeDto;
import org.demo.app.payload.AppResponse;
import org.demo.app.service.EmployeeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("demo")
            .withPassword("P@ssw0rd");

    static {
        postgres.start();
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;

    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    private String createURLWithPort() {
        return "http://localhost:" + port + "/api/v1/employees";
    }

    @Test
    @Order(1)
    void testCreateEmployeeList() {
        ResponseEntity<AppResponse<List<EmployeeDto>>> response = restTemplate.exchange(
                createURLWithPort() + "/create/5",
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<>() {
                });
        List<EmployeeDto> employeeDtos = response.getBody().getData();
        Assertions.assertNotNull(employeeDtos);
        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getBody().getStatusCode());
        Assertions.assertEquals(employeeDtos.size(), 5);
    }

    @Test
    @Order(2)
    void testEmployeesList() {
        ResponseEntity<AppResponse<List<EmployeeDto>>> response = restTemplate.exchange(
                createURLWithPort(),
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<>() {
                });
        List<EmployeeDto> employeeEntityList = response.getBody().getData();
        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getBody().getStatusCode());
        Assertions.assertNotNull(employeeEntityList);
        Assertions.assertEquals(employeeEntityList.size(), 5);
    }

    @Test
    @Order(3)
    void testFindEmployeeById() {
        ResponseEntity<AppResponse<EmployeeDto>> response = restTemplate.exchange(
                createURLWithPort() + "/1",
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<>() {
                });
        EmployeeDto employeeDto = response.getBody().getData();
        Assertions.assertNotNull(employeeDto);
        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getBody().getStatusCode());
    }

    @Test
    @Order(4)
    void testCountEmployees() {
        ResponseEntity<AppResponse<Long>> response = restTemplate.exchange(
                createURLWithPort() + "/count",
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<>() {
                });
        Long count = response.getBody().getData();
        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getBody().getStatusCode());
        Assertions.assertNotNull(count);
        Assertions.assertEquals(5L, count);
    }

    @Test
    @Order(5)
    void testUpdateEmployee() {
        EmployeeDto employee = employeeService.findById(1L);
        BigDecimal salary = employee.getSalary();
        BigDecimal newSalary = salary.add(new BigDecimal(5000));
        employee.setSalary(newSalary);
        ResponseEntity<AppResponse<EmployeeDto>> response = restTemplate.exchange(
                createURLWithPort() + "/1",
                HttpMethod.PUT,
                new HttpEntity<>(employee, headers),
                new ParameterizedTypeReference<>() {
                });
        EmployeeDto employeeDto = response.getBody().getData();
        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getBody().getStatusCode());
        Assertions.assertNotNull(employeeDto);
        Assertions.assertEquals(newSalary, employeeDto.getSalary());
    }

    @Test
    @Order(6)
    void testDeleteEmployee() {
        ResponseEntity<AppResponse<Void>> response = restTemplate.exchange(
                createURLWithPort() + "/1",
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<>() {
                });
        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.getBody().getStatusCode());
    }
}
