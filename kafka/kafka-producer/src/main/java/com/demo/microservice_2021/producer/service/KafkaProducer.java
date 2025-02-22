package com.demo.microservice_2021.producer.service;

import org.apache.avro.specific.SpecificRecordBase;

import java.io.Serializable;

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {
    void sendMessage(String topicName, K key, V message);
}
