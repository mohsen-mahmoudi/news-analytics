package com.demo.microservice_2021.reactive.elastic.query.web.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain (ServerHttpSecurity http) {
        http.authorizeExchange()
                .anyExchange()
                .permitAll();
        http.csrf().disable();
        return http.build();
    }
}
