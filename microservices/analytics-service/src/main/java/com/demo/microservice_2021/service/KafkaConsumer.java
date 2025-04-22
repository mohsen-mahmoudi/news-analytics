package com.demo.microservice_2021.service;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

public interface KafkaConsumer<T extends SpecificRecordBase> {
    //public void onAppStarted();
    void receive(List<T> messages, List<String> keys, List<Integer> partitions, List<Long> offsets);
}
