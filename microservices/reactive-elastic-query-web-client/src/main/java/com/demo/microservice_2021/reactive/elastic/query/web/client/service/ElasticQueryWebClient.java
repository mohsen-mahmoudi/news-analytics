package com.demo.microservice_2021.reactive.elastic.query.web.client.service;

import com.demo.microservice_2021.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.demo.microservice_2021.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;
import reactor.core.publisher.Flux;

public interface ElasticQueryWebClient {
    Flux<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel requestModel);
}
