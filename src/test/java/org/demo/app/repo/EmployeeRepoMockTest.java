package org.demo.app.repo;


import org.assertj.core.api.Assertions;
import org.demo.app.model.EmployeeEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
class EmployeeRepoMockTest {

    @MockBean
    private EmployeeRepo employeeRepo;

    @Test
    void testFindByEmail() {

        EmployeeEntity employee = EmployeeEntity.builder()
                .firstName("Ahmed")
                .lastName("Ali")
                .email("ahmed.ali@tesla.com")
                .salary(new BigDecimal(10000))
                .joinDate(new Date())
                .build();


        when(employeeRepo.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(employee));
        Optional<EmployeeEntity> employeeEntity = employeeRepo.findByEmail("ahmed.ali@tesla.com");
        Assertions.assertThat(employeeEntity.isPresent()).isTrue();
    }

    @Test
    void testSaveEmployee() {

        // 1. arrange

        EmployeeRepo repo = Mockito.mock(EmployeeRepo.class);


        EmployeeEntity employee = EmployeeEntity.builder()
                .firstName("Ahmed")
                .lastName("Ali")
                .email("ahmed.ali@tesla.com")
                .salary(new BigDecimal(10000))
                .joinDate(new Date())
                .build();

        // 2. act
        when(repo.save(Mockito.any(EmployeeEntity.class))).thenReturn(employee);
        EmployeeEntity savedEmployee = repo.save(employee);

        // 3. assert
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo("ahmed.ali@tesla.com");

    }

}
