package com.sololiving.global.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentationConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class AbstractRestDocsTests {

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected WebTestClientRestDocumentationConfigurer restDocs;

    @BeforeEach
    void setUp(final ApplicationContext context, final RestDocumentationContextProvider restDocumentation) {

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        this.webTestClient = WebTestClient.bindToApplicationContext(context)
                .configureClient()
                .exchangeStrategies(strategies)
                .filter(restDocs) // 주입된 restDocs 빈 사용
                .build();
    }
}
