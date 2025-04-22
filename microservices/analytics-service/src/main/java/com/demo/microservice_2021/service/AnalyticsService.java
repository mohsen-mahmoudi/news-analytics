package com.demo.microservice_2021.service;

import com.demo.microservice_2021.dto.AnalyticsResponseModel;

import java.util.Optional;

public interface AnalyticsService {
    Optional<AnalyticsResponseModel> getWordAnalytics(String word);
}
