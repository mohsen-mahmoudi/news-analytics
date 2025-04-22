package com.demo.microservice_2021;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnalyticsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnalyticsServiceApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner checkKafka(KafkaConsumer kafkaConsumer) {
//        return args -> {
//            kafkaConsumer.onAppStarted();
//        };
//    }
}
