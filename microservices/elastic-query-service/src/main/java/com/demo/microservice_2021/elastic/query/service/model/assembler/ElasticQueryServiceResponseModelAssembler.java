package com.demo.microservice_2021.elastic.query.service.model.assembler;

import com.demo.microservice_2021.elastic.model.index.impl.NewsIndexModel;
import com.demo.microservice_2021.elastic.query.service.api.ElasticDocumentApi;
import com.demo.microservice_2021.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.demo.microservice_2021.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ElasticQueryServiceResponseModelAssembler extends
        RepresentationModelAssemblerSupport<NewsIndexModel, ElasticQueryServiceResponseModel> {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentApi.class);

    private final ElasticToResponseModelTransformer transformer;

    public ElasticQueryServiceResponseModelAssembler(ElasticToResponseModelTransformer transformer) {
        super(ElasticDocumentApi.class, ElasticQueryServiceResponseModel.class);
        this.transformer = transformer;
    }

    @Override
    public ElasticQueryServiceResponseModel toModel(NewsIndexModel entity) {
        ElasticQueryServiceResponseModel responseModel = transformer.getResponseModel(entity);
        responseModel.add(linkTo(methodOn(ElasticDocumentApi.class).getDocumentById(entity.getId())).withSelfRel());
        responseModel.add(linkTo(ElasticDocumentApi.class).withRel("documents"));
        return responseModel;
    }

    public List<ElasticQueryServiceResponseModel> toModels(List<NewsIndexModel> entity) {
        return entity.stream().map(this::toModel).toList();
    }
}
