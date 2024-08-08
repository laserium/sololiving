package com.sololiving.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ThymeleafConfig {

    @Bean
    public SpringResourceTemplateResolver emailTemplateResolver() {
        SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
        emailTemplateResolver.setPrefix("classpath:/templates/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        emailTemplateResolver.setOrder(1);
        emailTemplateResolver.setCheckExistence(true);
        return emailTemplateResolver;
    }

    @Bean
    public SpringResourceTemplateResolver docsTemplateResolver() {
        SpringResourceTemplateResolver docsTemplateResolver = new SpringResourceTemplateResolver();
        docsTemplateResolver.setPrefix("classpath:/static/docs/");
        docsTemplateResolver.setSuffix(".html");
        docsTemplateResolver.setTemplateMode(TemplateMode.HTML);
        docsTemplateResolver.setCharacterEncoding("UTF-8");
        docsTemplateResolver.setOrder(2);
        docsTemplateResolver.setCheckExistence(true);
        return docsTemplateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(emailTemplateResolver());
        templateEngine.addTemplateResolver(docsTemplateResolver());
        return templateEngine;
    }
}
