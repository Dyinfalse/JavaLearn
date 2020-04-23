package com.jgmt.blog;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 使用Configuration 注解之后可以在Service 里使用@Autowrite 注解自动导入
 */
@Configuration
public class RestConfiguration {

    private final RestTemplateBuilder builder;

    public RestConfiguration(RestTemplateBuilder builder) {
        this.builder = builder;
    }

    @Bean
    public RestTemplate restTemplate(){

        return builder.build();
    }
}
