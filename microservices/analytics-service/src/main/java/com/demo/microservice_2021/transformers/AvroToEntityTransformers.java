package com.demo.microservice_2021.transformers;

import com.demo.microservice_2021.entity.AnalyticsEntity;
import com.demo.microservice_2021.kafka.avro.model.NewsAnalyticsAvroModel;
import org.springframework.stereotype.Component;
import org.springframework.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AvroToEntityTransformers {

    private final IdGenerator idGenerator;

    public AvroToEntityTransformers(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public List<AnalyticsEntity> getEntities(List<NewsAnalyticsAvroModel> avroModelList) {
        return avroModelList.stream()
                .map(avroModel -> new AnalyticsEntity(
                        idGenerator.generateId(),
                        avroModel.getWord(),
                        avroModel.getWordCount(),
                        LocalDateTime.now()
                ))
                .toList();
    }
}
