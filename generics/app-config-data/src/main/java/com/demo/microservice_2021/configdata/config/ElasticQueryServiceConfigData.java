package com.demo.microservice_2021.configdata.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "elastic-query-service-config")
public class ElasticQueryServiceConfigData {
    private String version;
    private Long backPressureDelayMs;
    private String customAudience;
}
