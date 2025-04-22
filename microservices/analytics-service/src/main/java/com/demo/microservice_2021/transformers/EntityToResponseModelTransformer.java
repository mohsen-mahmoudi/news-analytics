package com.demo.microservice_2021.transformers;

import com.demo.microservice_2021.dto.AnalyticsResponseModel;
import com.demo.microservice_2021.entity.AnalyticsEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EntityToResponseModelTransformer {

    public Optional<AnalyticsResponseModel> getResponseModel(AnalyticsEntity analyticsEntity) {
        if (analyticsEntity == null) {
            return Optional.empty();
        }
        return Optional.of(new AnalyticsResponseModel(
                analyticsEntity.getId(),
                analyticsEntity.getWord(),
                analyticsEntity.getWordCount()
        ));
    }
}
