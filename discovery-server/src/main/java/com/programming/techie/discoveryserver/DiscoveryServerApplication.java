package com.programming.techie.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {
    
    // eureka-server 에 쓰는 Application
    // application.properties 에서
    // 자신을 client로 등록하지 않아도 됨 (false 처리)
    // 또한, local에서 registry를 처리하기에 false 처리
    // eureka는 기본 port가 8761이다.
    // localhost:8761 에서 Eureka 의 대시보드를 볼수있다.
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }
}
    