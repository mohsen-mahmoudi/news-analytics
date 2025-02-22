package com.demo.microservice_2021.newsToKafka.service;

import com.demo.microservice_2021.newsToKafka.dto.Article;

public interface NewsToKafkaService {
    public void streamNewsToKafka(Article article);
}
