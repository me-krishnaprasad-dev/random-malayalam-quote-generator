package com.kp.random_malayalam_quote_generator.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * REST Template Configuration
 * This configuration creates a RestTemplate bean for making HTTP calls to external APIs
 *
 * @author Krishna Prasad A
 * @since 31-03-2026
 */
@Configuration
public class GeminiChatClientConfig {

    /**
     * Create RestTemplate bean for HTTP calls
     *
     * @param builder RestTemplateBuilder
     * @return RestTemplate bean
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
