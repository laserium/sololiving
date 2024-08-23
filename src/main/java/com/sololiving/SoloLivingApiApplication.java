package com.sololiving;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

// CI/CD TEST 35
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAspectJAutoProxy
public class SoloLivingApiApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SoloLivingApiApplication.class)
                .profiles("develop")
                .run(args);
    }

}