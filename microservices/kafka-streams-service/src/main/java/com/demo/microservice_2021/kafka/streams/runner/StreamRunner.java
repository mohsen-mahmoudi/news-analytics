package com.demo.microservice_2021.kafka.streams.runner;

import org.springframework.boot.CommandLineRunner;

public interface StreamRunner extends CommandLineRunner {

    Long getValueByKey(String key);
}
