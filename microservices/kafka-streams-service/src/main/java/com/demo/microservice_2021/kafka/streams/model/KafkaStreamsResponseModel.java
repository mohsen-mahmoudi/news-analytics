package com.demo.microservice_2021.kafka.streams.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaStreamsResponseModel {
    private String word;
    private Long wordCount;
}
