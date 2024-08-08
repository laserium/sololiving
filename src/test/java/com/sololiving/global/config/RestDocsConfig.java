package com.sololiving.global.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentationConfigurer;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

@TestConfiguration
public class RestDocsConfig {

    @Bean
    public WebTestClientRestDocumentationConfigurer restDocs(RestDocumentationContextProvider restDocumentation) {
        WebTestClientRestDocumentationConfigurer configurer = WebTestClientRestDocumentation
                .documentationConfiguration(restDocumentation);
        configurer.operationPreprocessors()
                .withRequestDefaults(Preprocessors.prettyPrint())
                .withResponseDefaults(Preprocessors.prettyPrint());
        return configurer;
    }
}
