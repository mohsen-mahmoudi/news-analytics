package com.demo.microservice_2021.elastic.index.client.service.impl;

import com.demo.microservice_2021.configdata.config.ElasticConfigData;
import com.demo.microservice_2021.elastic.index.client.service.ElasticIndexClient;
import com.demo.microservice_2021.elastic.index.client.util.ElasticIndexUtil;
import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "false")
public class NewsElasticIndexClientImpl implements ElasticIndexClient<NewsIndexModel> {

    private static final Logger logger = LoggerFactory.getLogger(NewsElasticIndexClientImpl.class);

    private final ElasticConfigData elasticConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticIndexUtil<NewsIndexModel> newsIndexModelElasticIndexUtil;

    public NewsElasticIndexClientImpl(ElasticConfigData elasticConfigData,
                                      ElasticsearchOperations elasticsearchOperations,
                                      ElasticIndexUtil<NewsIndexModel> newsIndexModelElasticIndexUtil) {
        this.elasticConfigData = elasticConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.newsIndexModelElasticIndexUtil = newsIndexModelElasticIndexUtil;
    }

    @Override
    public List<String> save(List<NewsIndexModel> documents) {
        List<IndexQuery> indexQueryList = newsIndexModelElasticIndexUtil.getIndexQueryList(documents);
        List<IndexedObjectInformation> indexedObjectInformation = elasticsearchOperations.bulkIndex(
                indexQueryList, IndexCoordinates.of(elasticConfigData.getIndexName()));
        logger.info("Indexed {} documents", indexedObjectInformation.size());
        return indexedObjectInformation.stream().map(IndexedObjectInformation::getId).toList();
    }

}
