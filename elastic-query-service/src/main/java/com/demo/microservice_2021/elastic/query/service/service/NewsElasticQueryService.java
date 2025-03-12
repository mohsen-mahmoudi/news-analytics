package com.demo.microservice_2021.elastic.query.service.service;

import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import com.demo.microservice_2021.elastic.query.service.api.ElasticDocumentApi;
import com.demo.microservice_2021.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.demo.microservice_2021.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import com.demo.microservice_2021.query.client.service.ElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentApi.class);

    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;
    private final ElasticQueryClient<NewsIndexModel> newsIndexModelElasticQueryClient;

    public NewsElasticQueryService(ElasticToResponseModelTransformer elasticToResponseModelTransformer,
                                   ElasticQueryClient<NewsIndexModel> newsIndexModelElasticQueryClient) {
        this.elasticToResponseModelTransformer = elasticToResponseModelTransformer;
        this.newsIndexModelElasticQueryClient = newsIndexModelElasticQueryClient;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(Long id) {
        LOG.info("Querying to elasticsearch by id {}", id);
        return elasticToResponseModelTransformer.getResponseModel(
                newsIndexModelElasticQueryClient.getIndexModelById(id.toString())
        );
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByValue(String value) {
        LOG.info("Querying to elasticsearch by value {}", value);
        return elasticToResponseModelTransformer.getResponseModels(
                newsIndexModelElasticQueryClient.getIndexModelByField(value)
        );
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocument() {
        LOG.info("Querying to elasticsearch for all documents");
        return elasticToResponseModelTransformer.getResponseModels(
                newsIndexModelElasticQueryClient.getAllIndexModel()
        );
    }
}
