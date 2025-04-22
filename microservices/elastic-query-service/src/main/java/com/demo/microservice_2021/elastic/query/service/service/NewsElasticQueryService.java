package com.demo.microservice_2021.elastic.query.service.service;

import com.demo.microservice_2021.configdata.config.ElasticQueryServiceConfigData;
import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import com.demo.microservice_2021.elastic.query.service.api.ElasticDocumentApi;
import com.demo.microservice_2021.elastic.query.service.common.exception.ElasticQueryServiceException;
import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.demo.microservice_2021.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;
import com.demo.microservice_2021.elastic.query.service.model.ElasticQueryServiceResponseModelAssembler;
import com.demo.microservice_2021.elastic.query.service.model.ElasticQueryServiceWordCountResponseModel;
import com.demo.microservice_2021.elastic.query.service.transformers.QueryType;
import com.demo.microservice_2021.query.client.service.ElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class NewsElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentApi.class);

    private final ElasticQueryClient<NewsIndexModel> newsIndexModelElasticQueryClient;
    private final ElasticQueryServiceResponseModelAssembler assembler;
    private final ElasticQueryServiceConfigData configData;
    private final WebClient.Builder webClientBuilder;

    public NewsElasticQueryService(ElasticQueryClient<NewsIndexModel> newsIndexModelElasticQueryClient,
                                   ElasticQueryServiceResponseModelAssembler assembler,
                                   ElasticQueryServiceConfigData configData,
                                   @Qualifier("webClientBuilder") WebClient.Builder webClientBuilder) {
        this.newsIndexModelElasticQueryClient = newsIndexModelElasticQueryClient;
        this.assembler = assembler;
        this.configData = configData;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(Long id) {
        LOG.info("Querying to elasticsearch by id {}", id);
        return assembler.toModel(newsIndexModelElasticQueryClient.getIndexModelById(id.toString()));
    }

    @Override
    public ElasticQueryServiceAnalyticsResponseModel getDocumentByValue(String value, String accessToken) {
        LOG.info("Querying to elasticsearch by value {}", value);
        List<ElasticQueryServiceResponseModel> assemblerModels =
                assembler.toModels(newsIndexModelElasticQueryClient.getIndexModelByField(value));
        return ElasticQueryServiceAnalyticsResponseModel.builder()
                .queryResponseModels(assemblerModels)
                .wordCount(getWordCount(value, accessToken))
                .build();
    }

    private Long getWordCount(String value, String accessToken) {
        if (QueryType.KAFKA_STATE_STORE.getType().equals(configData.getWebclient().getQueryType())) {
            return getFromKafkaStateStore(value, accessToken).getWordCount();
        } else if(QueryType.ANALYTICS_DATABASE.getType().equals(configData.getWebclient().getQueryType())) {
            return getFromAnalyticsDatabase(value, accessToken).getWordCount();
        }
        return 0L;
    }

    private ElasticQueryServiceWordCountResponseModel getFromAnalyticsDatabase(String value, String accessToken) {
        ElasticQueryServiceConfigData.QueryFrom kafkaStateStore = configData.getQueryFromKafkaStateStore();
        return retrieveResponseModel(value, accessToken, kafkaStateStore);
    }

    private ElasticQueryServiceWordCountResponseModel getFromKafkaStateStore(String value, String accessToken) {
        ElasticQueryServiceConfigData.QueryFrom analyticsDatabase = configData.getQueryFromAnalyticsDatabase();
        return retrieveResponseModel(value, accessToken, analyticsDatabase);
    }

    private ElasticQueryServiceWordCountResponseModel retrieveResponseModel(
            String value,
            String accessToken,
            ElasticQueryServiceConfigData.QueryFrom storeType) {
        return webClientBuilder.build()
                .method(HttpMethod.valueOf(storeType.getMethod()))
                .uri(storeType.getUrl(), uri -> uri.build(value))
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(
                        httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not Authenticated!")))
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> Mono.just(new ElasticQueryServiceException(clientResponse.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase())))
                .bodyToMono(ElasticQueryServiceWordCountResponseModel.class)
                .log()
                .block();
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocument() {
        LOG.info("Querying to elasticsearch for all documents");
        return assembler.toModels(newsIndexModelElasticQueryClient.getAllIndexModel());
    }
}
