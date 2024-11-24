package org.demo.app.repo;


import org.demo.app.model.EmployeeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepoTest {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Test
    @Sql(statements = "insert into employees(id, first_name, last_name, email,salary) values (5666, 'Ahmed','Ali','ahmed.ali@tesla.com',10000)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "delete from employees where id=5666", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testFindByEmail_shouldReturnEmployee() {
        Optional<EmployeeEntity> employee = employeeRepo.findByEmail("ahmed.ali@tesla.com");
        Assertions.assertTrue(employee.isPresent());
    }

    @Test
    void testFindByNonExistEmail_shouldReturnNoEmployee() {
        Optional<EmployeeEntity> employee = employeeRepo.findByEmail("ahmed.hassan@tesla.com");
        Assertions.assertFalse(employee.isPresent());
    }

}
