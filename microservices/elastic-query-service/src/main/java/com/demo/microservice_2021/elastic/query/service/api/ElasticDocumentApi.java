package com.demo.microservice_2021.elastic.query.service.api;

import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceRequestModel;
import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.demo.microservice_2021.elastic.query.service.service.ElasticQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "/documents")
public class ElasticDocumentApi {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentApi.class);

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentApi(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @Operation(summary = "Get all documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all documents"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Not found"),
    })
    @ResponseBody
    @GetMapping(value = {"/", ""})
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocuments() {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocument();
        LOG.info("Elasticsearch returned {} documents", response.size());
        return ResponseEntity.ok(response);
    }

    @PostAuthorize("hasPermission(#id, 'ElasticQueryServiceResponseModel', 'READ')")
    @Operation(summary = "Get document by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all documents"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Not found"),
    })
    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty Long id) {
        ElasticQueryServiceResponseModel response = elasticQueryService.getDocumentById(id);
        LOG.info("Elasticsearch returned {} document by id {}", response, id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('APP_USER_ROLE') || hasRole('APP_SUPER_USER_ROLE') || hasAuthority('SCOPE_APP_USER_ROLE')")
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @Operation(summary = "Get document by value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all documents"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Not found"),
    })
    @ResponseBody
    @PostMapping("/get-document-by-value")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>>
    getDocumentByValue(@RequestBody @Valid ElasticQueryServiceRequestModel requestModel) {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getDocumentByValue(requestModel.getValue());
        LOG.info("Elasticsearch returned {} document by value {}", response, requestModel.getValue());
        return ResponseEntity.ok(response);
    }
}
