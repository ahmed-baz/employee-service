package org.demo.app.service;

import org.demo.app.dto.EmployeeDto;

import java.io.IOException;
import java.util.List;


public interface EmployeeService {

    void createRandomList(int size);

    List<EmployeeDto> findList();

    EmployeeDto findById(Long id);

    EmployeeDto findByEmail(String email);

    Long count();

    EmployeeDto create(EmployeeDto employeeDto);

    EmployeeDto update(Long id, EmployeeDto employeeDto);

    void delete(Long id);

    void deleteAll();
}
