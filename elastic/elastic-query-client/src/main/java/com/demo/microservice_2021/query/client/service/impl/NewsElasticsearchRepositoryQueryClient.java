package com.demo.microservice_2021.query.client.service.impl;

import com.demo.microservice_2021.common.util.CollectionsUtil;
import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import com.demo.microservice_2021.query.client.exception.ElasticQueryClientException;
import com.demo.microservice_2021.query.client.repository.NewsElasticsearchQueryRepository;
import com.demo.microservice_2021.query.client.service.ElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class NewsElasticsearchRepositoryQueryClient implements ElasticQueryClient<NewsIndexModel> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final NewsElasticsearchQueryRepository newsElasticsearchQueryRepository;

    public NewsElasticsearchRepositoryQueryClient(NewsElasticsearchQueryRepository newsElasticsearchQueryRepository) {
        this.newsElasticsearchQueryRepository = newsElasticsearchQueryRepository;
    }

    @Override
    public NewsIndexModel getIndexModelById(String id) {
        Optional<NewsIndexModel> byId = newsElasticsearchQueryRepository.findById(id);
        LOG.info("News Document found for id: {}", byId
                .orElseThrow(()-> new ElasticQueryClientException("News Document not found for id: " + id))
                .getId());
        return byId.get();
    }

    @Override
    public List<NewsIndexModel> getIndexModelByField(String value) {
        List<NewsIndexModel> byText = newsElasticsearchQueryRepository.findByText(value);
        LOG.info("{} of News Document found for field: {}", byText.size(), value);
        return byText;
    }

    @Override
    public List<NewsIndexModel> getAllIndexModel() {
        Iterable<NewsIndexModel> all = newsElasticsearchQueryRepository.findAll();
        List<NewsIndexModel> listFromIterable = CollectionsUtil.getInstance().getListFromIterable(all);
        LOG.info("{} of News Document found for field: {}", listFromIterable.size());
        return listFromIterable;
    }
}
