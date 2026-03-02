package com.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    // explicit routes (llb) to services registered in Eureka (service ids are lower-case due to property set)
//    @Bean
//    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("student_route", r -> r.path("/students/**").uri("lb://student-service"))
//                .route("school_route", r -> r.path("/school/**").uri("lb://school-service"))
//                .build();
//    }
}
