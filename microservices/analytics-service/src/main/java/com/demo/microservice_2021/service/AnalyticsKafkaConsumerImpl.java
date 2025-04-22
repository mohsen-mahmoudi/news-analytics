package com.demo.microservice_2021.service;

import com.demo.microservice_2021.configdata.config.KafkaConfigData;
import com.demo.microservice_2021.entity.AnalyticsEntity;
import com.demo.microservice_2021.kafak.admin.client.KafkaAdminClient;
import com.demo.microservice_2021.kafka.avro.model.NewsAnalyticsAvroModel;
import com.demo.microservice_2021.repository.AnalyticsRepository;
import com.demo.microservice_2021.transformers.AvroToEntityTransformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AnalyticsKafkaConsumerImpl implements KafkaConsumer<NewsAnalyticsAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsKafkaConsumerImpl.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;
    private final AvroToEntityTransformers avroToEntityTransformers;
    private final AnalyticsRepository analyticsRepository;

    public AnalyticsKafkaConsumerImpl(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                                      KafkaAdminClient kafkaAdminClient,
                                      KafkaConfigData kafkaConfigData,
                                      AvroToEntityTransformers avroToEntityTransformers,
                                      AnalyticsRepository analyticsRepository) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        this.avroToEntityTransformers = avroToEntityTransformers;
        this.analyticsRepository = analyticsRepository;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void onAppStarted() {
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("Topics with names {} is ready for operation.", kafkaConfigData.getTopicNamesToCreate().toArray());
        Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer("newsAnalyticsTopicListener"))
                .start();
    }

    @Override
    @Transactional
    @KafkaListener(id = "newsAnalyticsTopicListener", topics = "${kafka-config.topic-name}", autoStartup = "false")
    public void receive(
            @Payload List<NewsAnalyticsAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        List<AnalyticsEntity> entities = avroToEntityTransformers.getEntities(messages);
        analyticsRepository.persist(entities);
        LOG.info("News analytics {} records persisted", entities.size());
    }
}
