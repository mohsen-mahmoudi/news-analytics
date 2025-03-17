package com.demo.microservice_2021.elastic.query.service.service;

import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import com.demo.microservice_2021.elastic.query.service.api.ElasticDocumentApi;
import com.demo.microservice_2021.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.demo.microservice_2021.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import com.demo.microservice_2021.query.client.service.ElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentApi.class);

    private final ElasticQueryClient<NewsIndexModel> newsIndexModelElasticQueryClient;
    private final ElasticQueryServiceResponseModelAssembler assembler;

    public NewsElasticQueryService(ElasticQueryClient<NewsIndexModel> newsIndexModelElasticQueryClient,
                                   ElasticQueryServiceResponseModelAssembler assembler) {
        this.newsIndexModelElasticQueryClient = newsIndexModelElasticQueryClient;
        this.assembler = assembler;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(Long id) {
        LOG.info("Querying to elasticsearch by id {}", id);
        return assembler.toModel(newsIndexModelElasticQueryClient.getIndexModelById(id.toString()));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByValue(String value) {
        LOG.info("Querying to elasticsearch by value {}", value);
        return assembler.toModels(newsIndexModelElasticQueryClient.getIndexModelByField(value));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocument() {
        LOG.info("Querying to elasticsearch for all documents");
        return assembler.toModels(newsIndexModelElasticQueryClient.getAllIndexModel());
    }
}
