package org.demo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EmpApp {

    public static void main(String[] args) {
        SpringApplication.run(EmpApp.class, args);
    }

}
