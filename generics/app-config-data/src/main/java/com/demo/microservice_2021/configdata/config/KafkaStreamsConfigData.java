package com.demo.microservice_2021.configdata.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "kafka-streams-config")
public class KafkaStreamsConfigData {

    private String customAudience;
    private String applicationId;
    private String inputTopicName;
    private String outputTopicName;
    private String stateFileLocation;
    private String wordCountStoreName;
}
