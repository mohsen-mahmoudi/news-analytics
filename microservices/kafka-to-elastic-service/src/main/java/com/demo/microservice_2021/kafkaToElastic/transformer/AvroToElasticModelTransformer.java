package com.demo.microservice_2021.kafkaToElastic.transformer;

import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import com.demo.microservice_2021.kafka.avro.model.NewsAvroModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class AvroToElasticModelTransformer {

    public List<NewsIndexModel> getElasticModel(List<NewsAvroModel> avroModels) {
        return avroModels.stream()
                .map(avroModel -> {
                    return NewsIndexModel.builder()
                            .userId(avroModel.getUserId())
                            .id(avroModel.getId())
                            .text(avroModel.getText())
                            .createdAt(convertTimestampToLocalDateTime(avroModel.getCreatedAt()))
                            .build();
                }).toList();
    }

    public LocalDateTime convertTimestampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

}
