package com.sololiving;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

// CI/CD TEST 82
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SoloLivingApiApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SoloLivingApiApplication.class)
                .profiles("develop")
                .run(args);
    }

}