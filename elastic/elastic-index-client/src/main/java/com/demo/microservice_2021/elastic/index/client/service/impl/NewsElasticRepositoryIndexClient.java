package com.demo.microservice_2021.elastic.index.client.service.impl;

import com.demo.microservice_2021.elastic.index.client.repository.NewsElasticsearchIndexRepository;
import com.demo.microservice_2021.elastic.index.client.service.ElasticIndexClient;
import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class NewsElasticRepositoryIndexClient implements ElasticIndexClient<NewsIndexModel> {

    private static final Logger logger = LoggerFactory.getLogger(NewsElasticIndexClientImpl.class);

    private final NewsElasticsearchIndexRepository newsElasticsearchIndexRepository;

    public NewsElasticRepositoryIndexClient(NewsElasticsearchIndexRepository newsElasticsearchIndexRepository) {
        this.newsElasticsearchIndexRepository = newsElasticsearchIndexRepository;
    }

    @Override
    public List<String> save(List<NewsIndexModel> documents) {
        List<NewsIndexModel> newsIndexModels = (List<NewsIndexModel>) newsElasticsearchIndexRepository.saveAll(documents);
        logger.info("Saved news index models: {}", newsIndexModels);
        return newsIndexModels.stream().map(NewsIndexModel::getId).toList();
    }
}
