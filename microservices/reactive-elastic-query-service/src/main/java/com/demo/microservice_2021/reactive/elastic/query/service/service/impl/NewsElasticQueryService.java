package com.demo.microservice_2021.reactive.elastic.query.service.service.impl;

import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.demo.microservice_2021.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import com.demo.microservice_2021.reactive.elastic.query.service.service.ElasticQueryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class NewsElasticQueryService implements ElasticQueryService {

    private final NewsReactiveElasticQueryClient newsReactiveElasticQueryClient;
    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;

    public NewsElasticQueryService(NewsReactiveElasticQueryClient newsReactiveElasticQueryClient,
                                   ElasticToResponseModelTransformer elasticToResponseModelTransformer) {
        this.newsReactiveElasticQueryClient = newsReactiveElasticQueryClient;
        this.elasticToResponseModelTransformer = elasticToResponseModelTransformer;
    }

    @Override
    public Flux<ElasticQueryServiceResponseModel> getDocumentByValue(String value) {
        return newsReactiveElasticQueryClient.getIndexModelByValue(value)
                .map(elasticToResponseModelTransformer::getResponseModel);
    }
}
