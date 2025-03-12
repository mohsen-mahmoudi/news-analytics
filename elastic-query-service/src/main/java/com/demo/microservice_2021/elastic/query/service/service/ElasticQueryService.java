package com.demo.microservice_2021.elastic.query.service.service;

import com.demo.microservice_2021.elastic.query.service.model.ElasticQueryServiceResponseModel;

import java.util.List;

public interface ElasticQueryService {

    ElasticQueryServiceResponseModel getDocumentById(Long id);
    List<ElasticQueryServiceResponseModel> getDocumentByValue(String value);
    List<ElasticQueryServiceResponseModel> getAllDocument();

}
