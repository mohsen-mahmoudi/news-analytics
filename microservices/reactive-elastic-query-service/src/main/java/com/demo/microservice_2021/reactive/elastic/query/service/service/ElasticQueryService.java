package com.demo.microservice_2021.reactive.elastic.query.service.service;

import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import reactor.core.publisher.Flux;

public interface ElasticQueryService {

    Flux<ElasticQueryServiceResponseModel> getDocumentByValue(String value);
}
