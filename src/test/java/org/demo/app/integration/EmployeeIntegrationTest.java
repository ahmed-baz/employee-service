package org.demo.app.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.app.model.EmployeeEntity;
import org.demo.app.payload.AppResponse;
import org.demo.app.repo.EmployeeRepo;
import org.demo.app.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private EmployeeService employeeService;

    private static HttpHeaders headers;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        Assertions.assertEquals(employeeEntityList.size(), employeeService.findList().size());
        Assertions.assertEquals(employeeEntityList.size(), employeeRepo.findAll().size());
    }
}
