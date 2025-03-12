package com.demo.microservice_2021.elastic.query.service.api;

import com.demo.microservice_2021.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.demo.microservice_2021.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.demo.microservice_2021.elastic.query.service.service.ElasticQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/documents")
public class ElasticDocumentApi {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentApi.class);

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentApi(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @ResponseBody
    @GetMapping("/")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocuments() {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocument();
        LOG.info("Elasticsearch returned {} documents", response.size());
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable Long id) {
        ElasticQueryServiceResponseModel response = elasticQueryService.getDocumentById(id);
        LOG.info("Elasticsearch returned {} document by id {}", response, id);
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @PostMapping("/get-document-by-value")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>>
    getDocumentByValue(@RequestBody ElasticQueryServiceRequestModel requestModel) {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getDocumentByValue(requestModel.getValue());
        LOG.info("Elasticsearch returned {} document by value {}", response, requestModel.getValue());
        return ResponseEntity.ok(response);
    }
}
