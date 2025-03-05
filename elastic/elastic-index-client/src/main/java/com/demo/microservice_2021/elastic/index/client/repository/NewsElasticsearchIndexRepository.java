package com.demo.microservice_2021.elastic.index.client.repository;

import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsElasticsearchIndexRepository extends ElasticsearchRepository<NewsIndexModel, String> {
}
