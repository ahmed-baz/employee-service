package org.demo.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.demo.app.dto.EmployeeDto;
import org.demo.app.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@RunWith(SpringRunner.class)
class EmployeeControllerTests {

    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private MockMvc mockMvc;
    private EmployeeDto employeeDto;
    private final List<EmployeeDto> employeeDtoList = new ArrayList<>();
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        EmployeeDto emp1 = EmployeeDto.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Ali")
                .email("ahmed.ali@gmail.com")
                .salary(new BigDecimal(15000))
                .joinDate(new Date())
                .build();
        EmployeeDto emp2 = EmployeeDto.builder()
                .id(2L)
                .firstName("Ahmed")
                .lastName("Hassan")
                .email("ahmed.hassan@gmail.com")
                .salary(new BigDecimal(20000))
                .joinDate(new Date())
                .build();
        employeeDtoList.add(emp1);
        employeeDtoList.add(emp2);
        employeeDto = emp1;
    }

    @Test
    @DisplayName("JUnit test for finding all employees")
    void testFindEmployeeList() throws Exception {
        when(employeeService.findList()).thenReturn(employeeDtoList);
        mockMvc.perform(get("/api/v1/employees"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(employeeDtoList.size())))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("JUnit test for creating dummy employee list")
    void testCreateEmployeeList() throws Exception {
        //when(employeeService.createRandomList(employeeDtoList.size())).thenReturn(employeeDtoList);
        mockMvc.perform(
                        get("/api/v1/employees/create/{size}", employeeDtoList.size())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(employeeDtoList.size())))
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.CREATED.value())))
                .andExpect(jsonPath("$.data").isArray());
    }


    @Test
    @DisplayName("JUnit test for finding employee by ID")
    void testFindEmployeeById() throws Exception {
        Long id = employeeDto.getId();
        when(employeeService.findById(id)).thenReturn(employeeDto);
        mockMvc.perform(get("/api/v1/employees/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.email", is(employeeDto.getEmail())))
                .andExpect(jsonPath("$.data.salary", is(15000)))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @DisplayName("JUnit test for finding employee by email")
    void testFindEmployeeByEmail() throws Exception {
        String email = employeeDto.getEmail();
        when(employeeService.findByEmail(email)).thenReturn(employeeDto);
        mockMvc.perform(get("/api/v1/employees/query?email={email}", email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.email", is(employeeDto.getEmail())))
                .andExpect(jsonPath("$.data.salary", is(15000)))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @DisplayName("JUnit test for counting employees")
    void testCountEmployees() throws Exception {
        String email = employeeDto.getEmail();
        when(employeeService.count()).thenReturn(25L);
        mockMvc.perform(get("/api/v1/employees/count"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data", is(25)));
    }

    @Test
    @DisplayName("JUnit test for creating new employee")
    void testCreateEmployee() throws Exception {
        EmployeeDto employee = EmployeeDto.builder()
                .firstName("Ahmed")
                .lastName("Ali")
                .email("ahmed.ali@gmail.com")
                .salary(new BigDecimal(20000))
                .build();
        //when(employeeService.create(employee)).thenReturn(employee);
        given(employeeService.create(employee)).willAnswer(invocation -> invocation.getArgument(0));
        MockHttpServletRequestBuilder httpServletRequestBuilder = post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee));

        mockMvc.perform(httpServletRequestBuilder)
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.CREATED.value())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("JUnit test for updating employee")
    void testUpdateEmployee() throws Exception {
        EmployeeDto employee = EmployeeDto.builder()
                .id(1L)
                .firstName("Ahmed")
                .lastName("Ali")
                .email("ahmed.ali@gmail.com")
                .salary(new BigDecimal(20000))
                .joinDate(new Date())
                .build();
        when(employeeService.update(1L, employee)).thenReturn(employee);
        mockMvc.perform(put("/api/v1/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee))
                )
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.OK.value())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("JUnit test for deleting employee")
    void testDeleteEmployeeById() throws Exception {
        doNothing().when(employeeService).delete(1L);
        mockMvc.perform(delete("/api/v1/employees/{id}", 1L))
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is(HttpStatus.NO_CONTENT.value())));
    }
}
