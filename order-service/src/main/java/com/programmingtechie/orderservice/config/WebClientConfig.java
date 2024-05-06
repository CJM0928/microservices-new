package com.programmingtechie.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// Spring Web Flux 프로젝트의 일부이기에 사용할수있음.
// Dependency에도 추가해줘야함.

@Configuration
public class WebClientConfig {

    // 웹 클라이언트용 Bean을 생성
    // 웹 클라이언트 빌드를 이용하여 웹 클라이언트의 인스턴스(WebClient Instance)를 생성할때마다 자동으로
    // 클라이언트측 로드밸런스를 통해 인벤토리 서비스의 포트를 찾아가게끔 도와준다.
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
