package com.demo.microservice_2021.service;

import com.demo.microservice_2021.dto.AnalyticsResponseModel;
import com.demo.microservice_2021.repository.AnalyticsRepository;
import com.demo.microservice_2021.transformers.EntityToResponseModelTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NewsAnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final EntityToResponseModelTransformer entityToResponseModelTransformer;

    public NewsAnalyticsServiceImpl(AnalyticsRepository repository,
                                    EntityToResponseModelTransformer transformer) {
        this.analyticsRepository = repository;
        this.entityToResponseModelTransformer = transformer;
    }

    @Override
    public Optional<AnalyticsResponseModel> getWordAnalytics(String word) {
        return entityToResponseModelTransformer.getResponseModel(analyticsRepository
                .getAnalyticsEntityByWordOrderByRecordDateDesc(word, PageRequest.of(0, 1))
                .stream().findFirst().orElse(null));
    }
}