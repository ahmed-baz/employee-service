package org.demo.app.service.impl;


import lombok.RequiredArgsConstructor;
import org.demo.app.dto.EmployeeDto;
import org.demo.app.model.EmployeeEntity;
import org.demo.app.repo.EmployeeRepo;
import org.demo.app.util.EmployeeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceDummy {

    private final EmployeeRepo employeeRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create() {
        EmployeeDto employeeDto = EmployeeUtil.createRandomEmployeeDto();
        EmployeeEntity employee = EmployeeEntity.builder()
                .firstName("user trx 2")
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .salary(employeeDto.getSalary())
                .joinDate(employeeDto.getJoinDate())
                .build();
        employeeRepo.save(employee);
    }

}
