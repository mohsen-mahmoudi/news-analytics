package com.demo.microservice_2021.elastic.query.web.client.service.impl;

import com.demo.microservice_2021.configdata.config.ElasticQueryWebClientServiceConfigData;
import com.demo.microservice_2021.elastic.query.web.client.common.exception.ElasticQueryWebClientException;
import com.demo.microservice_2021.elastic.query.web.client.common.model.ElasticQueryWebClientAnalyticsResponseModel;
import com.demo.microservice_2021.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.demo.microservice_2021.elastic.query.web.client.service.ElasticQueryWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NewsElasticQueryWebClient implements ElasticQueryWebClient {

    private static final Logger LOG = LoggerFactory.getLogger(NewsElasticQueryWebClient.class);

    private final WebClient.Builder webClientBuilder;
    private final ElasticQueryWebClientServiceConfigData configData;

    public NewsElasticQueryWebClient(@Qualifier("webClientBuilder") WebClient.Builder webClientBuilder,
                                     ElasticQueryWebClientServiceConfigData configData) {
        this.webClientBuilder = webClientBuilder;
        this.configData = configData;
    }

    @Override
    public ElasticQueryWebClientAnalyticsResponseModel getDataByText(ElasticQueryWebClientRequestModel request) {
        LOG.info("Querying by text {}", request.getValue());
        return getWebclient(request)
                .bodyToMono(ElasticQueryWebClientAnalyticsResponseModel.class)
                .log()
                .block(); // call sync
    }

    private WebClient.ResponseSpec getWebclient(ElasticQueryWebClientRequestModel request) {
        return webClientBuilder.build()
                .method(HttpMethod.valueOf(configData.getQueryByText().getMethod()))
                .uri(configData.getQueryByText().getUrl())
                .body(BodyInserters.fromPublisher(Mono.just(request), getRef()))
                .retrieve()
                .onStatus(
                        httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not Authenticated!")))
                .onStatus(
                        httpStatus -> httpStatus.is4xxClientError(),
                        clientResponse -> Mono.just(new ElasticQueryWebClientException(clientResponse.statusCode().getReasonPhrase())))
                .onStatus(
                        httpStatus -> httpStatus.is5xxServerError(),
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().getReasonPhrase())));
    }

    private <T> ParameterizedTypeReference<T> getRef() {
        return new ParameterizedTypeReference<T>() {
        };
    }
}
