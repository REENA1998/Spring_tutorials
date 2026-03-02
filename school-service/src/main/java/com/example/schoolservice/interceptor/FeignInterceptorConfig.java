package com.example.schoolservice.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class FeignInterceptorConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Add custom header before Feign sends the request
                template.header("X-Request-Source", "SchoolService");
                System.out.println("Added header: X-Request-Source = SchoolService");
            }
        };
    }
}
