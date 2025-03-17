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

    private Webclient webclient;
    private QueryByText queryByText;

    @Data
    public static class Webclient {
        private Integer connectionTimeoutMs;
        private Integer readTimeoutMs;
        private Integer writeTimeoutMs;
        private String maxInMemorySize;
        private String baseUrl;
    }

    @Data
    public static class QueryByText {
        private String method;
        private String url;
    }
}
