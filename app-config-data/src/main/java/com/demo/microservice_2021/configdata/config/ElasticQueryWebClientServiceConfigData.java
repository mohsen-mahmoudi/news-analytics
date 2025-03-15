package com.demo.microservice_2021.configdata.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "elastic-query-web-client-service-config")
public class ElasticQueryWebClientServiceConfigData {

    private WebclientConfigData webclient;

    @Data
    public static class WebclientConfigData {
        private Integer connectionTimeoutMs;
        private Integer readTimeoutMs;
        private Integer writeTimeoutMs;
        private Integer maxInMemorySize;
        private String baseUrl;
    }
}
