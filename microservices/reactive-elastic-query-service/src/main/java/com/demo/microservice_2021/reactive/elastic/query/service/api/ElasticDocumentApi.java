package com.demo.microservice_2021.reactive.elastic.query.service.api;

import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceRequestModel;
import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.demo.microservice_2021.reactive.elastic.query.service.service.ElasticQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RestController
@RequestMapping("/documents")
public class ElasticDocumentApi {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentApi.class);

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentApi(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @PostMapping(value = "/get-document-by-value",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ElasticQueryServiceResponseModel> getDocumentByValue(
            @RequestBody @Valid ElasticQueryServiceRequestModel requestModel) {
        LOG.info("Received request to get document by value: {}", requestModel.getValue());
        Flux<ElasticQueryServiceResponseModel> documentByValue = elasticQueryService.getDocumentByValue(requestModel.getValue());
        documentByValue = documentByValue.log();
        return documentByValue;
    }
}
