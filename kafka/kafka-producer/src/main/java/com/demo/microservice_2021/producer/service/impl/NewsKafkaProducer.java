package com.demo.microservice_2021.producer.service.impl;

import com.demo.microservice_2021.kafka.avro.model.NewsAvroModel;
import com.demo.microservice_2021.producer.service.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.checkerframework.checker.optional.qual.Present;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;

@Service
public class NewsKafkaProducer implements KafkaProducer<Long, NewsAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger(NewsKafkaProducer.class);

    private final KafkaTemplate<Long, NewsAvroModel> kafkaTemplate;

    public NewsKafkaProducer(KafkaTemplate<Long, NewsAvroModel> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(String topicName, Long key, NewsAvroModel message) {
        LOG.info("Producing message: {} to topic: {}", message, topicName);
        ListenableFuture<SendResult<Long, NewsAvroModel>> send = kafkaTemplate.send(topicName, key, message);
        callBack(send);
    }

    private static void callBack(ListenableFuture<SendResult<Long, NewsAvroModel>> send) {
        send.addCallback(result -> {
            RecordMetadata recordMetadata = result.getRecordMetadata();
            LOG.info("Receive Record Metadata: Topic: {}, Partition: {}, Offset: {}, Timestamp: {}",
                    recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
        }, ex -> {
            LOG.error("Error sending message: {}", ex.getMessage());
        });
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            kafkaTemplate.flush();
            kafkaTemplate.destroy();
            LOG.info("Closing Kafka Producer");
        }
    }
}
