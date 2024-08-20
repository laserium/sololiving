package com.sololiving;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

// CI/CD TEST 1
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAspectJAutoProxy
public class SoloLivingApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoloLivingApiApplication.class, args);
    }
}