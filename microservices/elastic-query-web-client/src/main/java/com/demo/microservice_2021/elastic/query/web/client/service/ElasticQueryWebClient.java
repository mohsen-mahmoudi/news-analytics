package com.demo.microservice_2021.elastic.query.web.client.service;


import com.demo.microservice_2021.elastic.query.web.client.common.model.ElasticQueryWebClientAnalyticsResponseModel;
import com.demo.microservice_2021.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;

public interface ElasticQueryWebClient {

    ElasticQueryWebClientAnalyticsResponseModel getDataByText(ElasticQueryWebClientRequestModel request);
}
