package com.demo.microservice_2021.query.client.service.impl;

import com.demo.microservice_2021.configdata.config.ElasticConfigData;
import com.demo.microservice_2021.configdata.config.ElasticQueryConfigData;
import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import com.demo.microservice_2021.query.client.exception.ElasticQueryClientException;
import com.demo.microservice_2021.query.client.service.ElasticQueryClient;
import com.demo.microservice_2021.query.client.util.ElasticQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsElasticQueryClient implements ElasticQueryClient<NewsIndexModel> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final ElasticConfigData elasticConfigData;
    private final ElasticQueryConfigData elasticQueryConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticQueryUtil<NewsIndexModel> elasticQueryUtil;

    public NewsElasticQueryClient(ElasticConfigData elasticConfigData,
                                  ElasticQueryConfigData elasticQueryConfigData,
                                  ElasticsearchOperations elasticsearchOperations,
                                  ElasticQueryUtil<NewsIndexModel> elasticQueryUtil) {
        this.elasticConfigData = elasticConfigData;
        this.elasticQueryConfigData = elasticQueryConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticQueryUtil = elasticQueryUtil;
    }

    @Override
    public NewsIndexModel getIndexModelById(String id) {
        Query searchById = elasticQueryUtil.getSearchById(id);
        SearchHit<NewsIndexModel> newsIndexModelSearchHit = elasticsearchOperations.searchOne(
                searchById, NewsIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));
        if (newsIndexModelSearchHit == null) {
            throw new ElasticQueryClientException("News Document not found for id: " + id);
        }
        LOG.info("News Document found for id: {}", id);
        return newsIndexModelSearchHit.getContent();
    }

    @Override
    public List<NewsIndexModel> getIndexModelByField(String value) {
        Query searchByField = elasticQueryUtil.getSearchByField(elasticQueryConfigData.getField(), value);
        SearchHits<NewsIndexModel> search = elasticsearchOperations.search(
                searchByField, NewsIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));
        LOG.info("{} of News Document found for field: {}", search.getTotalHits(), value);
        return search.get().map(SearchHit::getContent).toList();
    }

    @Override
    public List<NewsIndexModel> getAllIndexModel() {
        Query searchByField = elasticQueryUtil.getSearchForAll();
        SearchHits<NewsIndexModel> search = elasticsearchOperations.search(
                searchByField, NewsIndexModel.class, IndexCoordinates.of(elasticConfigData.getIndexName()));
        LOG.info("{} of News Document found", search.getTotalHits());
        return search.get().map(SearchHit::getContent).toList();
    }
}
