package com.demo.microservice_2021.kafkaToElastic.consumer.impl;

import com.demo.microservice_2021.configdata.config.KafkaConfigData;
import com.demo.microservice_2021.kafak.admin.client.KafkaAdminClient;
import com.demo.microservice_2021.kafka.avro.model.NewsAvroModel;
import com.demo.microservice_2021.kafkaToElastic.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumerImpl implements KafkaConsumer<Long, NewsAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerImpl.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;

    public KafkaConsumerImpl(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                             KafkaAdminClient kafkaAdminClient,
                             KafkaConfigData kafkaConfigData) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
    }

    @EventListener
    public void onAppStartup(ApplicationEvent applicationEvent) {
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("KafkaConsumerImpl.onAppStartup() :::: Topics check complete...");
        kafkaListenerEndpointRegistry.getListenerContainer("newsToKafkaListener").start();
    }

    @Override
    @KafkaListener(id = "newsToKafkaListener", topics = "${kafka-config.topic-name}")
    public void consume(@Payload List<NewsAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        LOG.info("Consumed message: {} with key: {} from partition: {} with offset: {}, with thread id: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString(), Thread.currentThread().getId());
    }
}
