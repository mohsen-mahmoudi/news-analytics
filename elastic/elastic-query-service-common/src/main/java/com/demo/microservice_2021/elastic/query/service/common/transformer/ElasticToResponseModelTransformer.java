package com.demo.microservice_2021.elastic.query.service.common.transformer;

import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElasticToResponseModelTransformer {
    public ElasticQueryServiceResponseModel getResponseModel(NewsIndexModel newsIndexModel) {
        return ElasticQueryServiceResponseModel.builder()
                .id(newsIndexModel.getId())
                .text(newsIndexModel.getText())
                .userId(newsIndexModel.getUserId())
                .createdAt(newsIndexModel.getCreatedAt())
                .build();
    }

    public List<ElasticQueryServiceResponseModel> getResponseModels(List<NewsIndexModel> newsIndexModels) {
        return newsIndexModels.stream().map(this::getResponseModel).toList();
    }
}

