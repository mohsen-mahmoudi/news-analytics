package com.demo.microservice_2021.elastic.index.client.service;

import com.demo.microservice_2021.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticIndexClient<T extends IndexModel> {
    List<String> save(List<T> documents);
}
