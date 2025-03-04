package com.demo.microservice_2021.configdata.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "elastic-config")
public class ElasticConfigData {
    private String indexName;
    private String connectionUrl;
    private Integer connectionTimoutMs;
    private Integer socketTimeoutMs;
}
