package org.demo.app.controller;


import org.demo.app.model.EmployeeEntity;
import org.demo.app.payload.AppResponse;
import org.demo.app.repo.EmployeeRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

import java.util.Collections;
import java.util.List;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepo employeeRepo;

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
    void testEmployeesList() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<AppResponse<List<EmployeeEntity>>> response = restTemplate.exchange(
                createURLWithPort(), HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                });
        List<EmployeeEntity> employeeEntityList = response.getBody().getData();
        Assertions.assertNotNull(employeeEntityList);
        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getBody().getStatusCode());
        Assertions.assertEquals(employeeEntityList.size(), employeeRepo.findAll().size());
    }
}
