package com.demo.microservice_2021.elastic.query.web.client.service;


import com.demo.microservice_2021.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.demo.microservice_2021.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;

import java.util.List;

public interface ElasticQueryWebClient {

    List<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel request);
}
