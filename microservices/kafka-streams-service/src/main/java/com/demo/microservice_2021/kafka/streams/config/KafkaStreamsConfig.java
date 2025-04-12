package com.demo.microservice_2021.kafka.streams.config;

import com.demo.microservice_2021.configdata.config.KafkaConfigData;
import com.demo.microservice_2021.configdata.config.KafkaStreamsConfigData;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {

    private final KafkaStreamsConfigData stream;
    private final KafkaConfigData kafka;

    public KafkaStreamsConfig(KafkaStreamsConfigData stream,
                              KafkaConfigData kafka) {
        this.stream = stream;
        this.kafka = kafka;
    }

    @Bean
    @Qualifier("kafkaStreamsConfigs")
    public Properties kafkaStreamsConfigs() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, stream.getApplicationId());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, kafka.getSchemaRegistryUrl());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, kafka.getReplicationFactor().intValue());
        props.put(StreamsConfig.STATE_DIR_CONFIG, stream.getStateFileLocation());
        return props;
    }
}
