package com.demo.microservice_2021.kafkaToElastic.consumer;

import org.apache.avro.specific.SpecificRecordBase;

import java.io.Serializable;
import java.util.List;

public interface KafkaConsumer<K extends Serializable, V extends SpecificRecordBase> {
    void consume(List<V> messages, List<Integer> keys, List<Integer> partitions, List<Long> offsets);
}
