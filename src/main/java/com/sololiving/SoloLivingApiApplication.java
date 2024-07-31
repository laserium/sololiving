package com.sololiving;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAspectJAutoProxy
// @MapperScan( basePackages = {"com.sololiving"})
public class SoloLivingApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SoloLivingApiApplication.class, args);
	}

}