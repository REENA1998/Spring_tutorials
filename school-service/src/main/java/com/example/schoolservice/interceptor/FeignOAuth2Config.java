package com.example.schoolservice.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Base64;


@Configuration
public class FeignOAuth2Config {
    private static final String CLIENT_ID = "dummyClient";
    private static final String CLIENT_SECRET = "dummySecret";

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 🧩 Simulate an encoded token (in real world, you'd call OAuth server to get one)
                String token = Base64.getEncoder()
                        .encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());

                String fakeJwt = "Bearer " + token;

                template.header("Authorization", fakeJwt);
                System.out.println("Added Authorization header: " + fakeJwt);
            }
        };
    }
}
