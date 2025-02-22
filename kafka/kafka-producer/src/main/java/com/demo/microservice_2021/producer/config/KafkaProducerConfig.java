package com.demo.microservice_2021.producer.config;

import com.demo.microservice_2021.configdata.config.KafkaConfigData;
import com.demo.microservice_2021.configdata.config.KafkaProducerConfigData;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ProducerFactory;

import java.io.Serializable;
import java.util.Map;

@Configuration
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducerConfigData kafkaProducerConfigData;

    public KafkaProducerConfig(KafkaConfigData kafkaConfigData, KafkaProducerConfigData kafkaProducerConfigData) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducerConfigData = kafkaProducerConfigData;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        return Map.of(
                "bootstrap.servers", kafkaConfigData.getBootstrapServers(),
                "key.serializer", kafkaProducerConfigData.getKeySerializerClass(),
                "value.serializer", kafkaProducerConfigData.getValueSerializerClass(),
                "compression.type", kafkaProducerConfigData.getCompressionType(),
                "acks", kafkaProducerConfigData.getAcks(),
                "batch.size", kafkaProducerConfigData.getBatchSize(),
                "batch.size.boost.size", kafkaProducerConfigData.getBatchSizeBoostSize(),
                "linger.ms", kafkaProducerConfigData.getLingerMs(),
                "request.timeout.ms", kafkaProducerConfigData.getRequestTimeoutMs(),
                "retries", kafkaProducerConfigData.getRetryCount()
        );
    }

    @Bean
    public ProducerFactory<K, V> producerFactory() {
        return new org.springframework.kafka.core.DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public org.springframework.kafka.core.KafkaTemplate<K, V> kafkaTemplate() {
        return new org.springframework.kafka.core.KafkaTemplate<>(producerFactory());
    }
}
