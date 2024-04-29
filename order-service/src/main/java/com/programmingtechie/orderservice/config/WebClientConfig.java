package com.programmingtechie.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// Spring Web Flux 프로젝트의 일부이기에 사용할수있음.
// Dependency에도 추가해줘야함.

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
