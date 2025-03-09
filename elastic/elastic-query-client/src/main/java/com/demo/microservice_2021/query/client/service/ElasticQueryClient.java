package com.demo.microservice_2021.query.client.service;

import com.demo.microservice_2021.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticQueryClient<T extends IndexModel> {

    T getIndexModelById(String id);

    List<T> getIndexModelByField(String value);

    List<T> getAllIndexModel();
}
