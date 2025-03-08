package com.demo.microservice_2021.kafkaToElastic.consumer.impl;

import com.demo.microservice_2021.configdata.config.KafkaConfigData;
import com.demo.microservice_2021.configdata.config.KafkaConsumerConfigData;
import com.demo.microservice_2021.elastic.index.client.service.ElasticIndexClient;
import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import com.demo.microservice_2021.kafak.admin.client.KafkaAdminClient;
import com.demo.microservice_2021.kafka.avro.model.NewsAvroModel;
import com.demo.microservice_2021.kafkaToElastic.consumer.KafkaConsumer;
import com.demo.microservice_2021.kafkaToElastic.transformer.AvroToElasticModelTransformer;
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
import java.util.Objects;

@Service
public class NewsKafkaConsumerImpl implements KafkaConsumer<Long, NewsAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger(NewsKafkaConsumerImpl.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;
    private final AvroToElasticModelTransformer avroToElasticModelTransformer;
    private final ElasticIndexClient<NewsIndexModel> newsIndexModelElasticIndexClient;

    public NewsKafkaConsumerImpl(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                                 KafkaAdminClient kafkaAdminClient,
                                 KafkaConfigData kafkaConfigData,
                                 KafkaConsumerConfigData kafkaConsumerConfigData,
                                 AvroToElasticModelTransformer avroToElasticModelTransformer,
                                 ElasticIndexClient<NewsIndexModel> newsIndexModelElasticIndexClient) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
        this.avroToElasticModelTransformer = avroToElasticModelTransformer;
        this.newsIndexModelElasticIndexClient = newsIndexModelElasticIndexClient;
    }

    @EventListener
    public void onAppStartup(ApplicationEvent applicationEvent) {
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("KafkaConsumerImpl.onAppStartup() :::: Topics {} check complete...", kafkaConfigData.getTopicNamesToCreate().toArray());
        Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer(
                kafkaConsumerConfigData.getConsumerGroupId())).start();
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
    public void consume(@Payload List<NewsAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        LOG.info("NewsKafkaConsumerImpl.consume() :::: Consumed message: {} with key: {} from partition: {} with offset: {}, with thread id: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString(), Thread.currentThread().getId());
        List<NewsIndexModel> elasticModels = avroToElasticModelTransformer.getElasticModel(messages);
        LOG.info("NewsKafkaConsumerImpl.consume() :::: Transformed {} models", elasticModels);
        List<String> documentIds = newsIndexModelElasticIndexClient.save(elasticModels);
        LOG.info("NewsKafkaConsumerImpl.consume() :::: Saved {} models with response: {}", elasticModels.size(), documentIds);
    }

}
