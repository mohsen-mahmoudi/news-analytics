package com.demo.microservice_2021.reactive.elastic.query.service.service;

import com.demo.microservice_2021.elastic.model.index.IndexModel;
import reactor.core.publisher.Flux;

public interface ReactiveElasticQueryClient<T extends IndexModel> {
    Flux<T> getIndexModelByValue(String value);
}
