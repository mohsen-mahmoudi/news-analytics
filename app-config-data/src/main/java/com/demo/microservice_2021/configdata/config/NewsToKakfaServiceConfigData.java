package com.demo.microservice_2021.configdata.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "news-to-kafka-service")
public class NewsToKakfaServiceConfigData {
    private String newsApiBearerToken;
    private String newsApiStreamUrl;
    private String newsKeyword;
}
