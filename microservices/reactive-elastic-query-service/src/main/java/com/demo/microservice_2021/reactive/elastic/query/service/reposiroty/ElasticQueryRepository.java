package com.demo.microservice_2021.reactive.elastic.query.service.reposiroty;

import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ElasticQueryRepository extends ReactiveCrudRepository<NewsIndexModel, String> {
    Flux<NewsIndexModel> findByText(String text);
}
