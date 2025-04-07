package com.demo.microservice_2021.reactive.elastic.query.service.service.impl;

import com.demo.microservice_2021.configdata.config.ElasticQueryServiceConfigData;
import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import com.demo.microservice_2021.reactive.elastic.query.service.reposiroty.ElasticQueryRepository;
import com.demo.microservice_2021.reactive.elastic.query.service.service.ReactiveElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class NewsReactiveElasticQueryClient implements ReactiveElasticQueryClient<NewsIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(NewsReactiveElasticQueryClient.class);

    private final ElasticQueryRepository elasticQueryRepository;
    private final ElasticQueryServiceConfigData configData;

    public NewsReactiveElasticQueryClient(ElasticQueryRepository elasticQueryRepository,
                                          ElasticQueryServiceConfigData configData) {
        this.elasticQueryRepository = elasticQueryRepository;
        this.configData = configData;
    }

    @Override
    public Flux<NewsIndexModel> getIndexModelByValue(String value) {
        LOG.info("Searching for news index model by value: {}", value);
        return elasticQueryRepository
                .findByText(value)
                .delayElements(Duration.ofMillis(configData.getBackPressureDelayMs()));
    }
}
