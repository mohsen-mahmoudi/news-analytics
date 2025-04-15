package com.demo.microservice_2021.elastic.query.service.service;

import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.demo.microservice_2021.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;

import java.util.List;

public interface ElasticQueryService {

    ElasticQueryServiceResponseModel getDocumentById(Long id);
    ElasticQueryServiceAnalyticsResponseModel getDocumentByValue(String value, String accessToken);
    List<ElasticQueryServiceResponseModel> getAllDocument();

}
