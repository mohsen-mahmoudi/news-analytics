package com.demo.microservice_2021.query.client.repository;

import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsElasticsearchQueryRepository extends ElasticsearchRepository<NewsIndexModel, String> {

    List<NewsIndexModel> findByText(String text);
}
