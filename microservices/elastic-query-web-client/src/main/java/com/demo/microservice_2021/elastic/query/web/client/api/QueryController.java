package com.demo.microservice_2021.elastic.query.web.client.api;

import com.demo.microservice_2021.elastic.query.web.client.common.model.ElasticQueryWebClientAnalyticsResponseModel;
import com.demo.microservice_2021.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.demo.microservice_2021.elastic.query.web.client.service.ElasticQueryWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class QueryController {

    private static final Logger LOG = LoggerFactory.getLogger(QueryController.class);

    private final ElasticQueryWebClient elasticQueryWebClient;

    public QueryController(ElasticQueryWebClient elasticQueryWebClient) {
        this.elasticQueryWebClient = elasticQueryWebClient;
    }

    @GetMapping
    public String index() {
        LOG.info("Index page requested");
        return "index";
    }

    @GetMapping("/error")
    public String error() {
        LOG.info("Error page requested");
        return "error";
    }

    @GetMapping("/home")
    public String home(Model model) {
        LOG.info("Home page requested");
        model.addAttribute("elasticQueryWebClientRequestModel",
                ElasticQueryWebClientRequestModel.builder().build());

        return "home";
    }

    @PostMapping("query-by-value")
    public String queryByText(@Valid ElasticQueryWebClientRequestModel requestModel, Model model) {
        LOG.info("Query by text requested: {}", requestModel);
        ElasticQueryWebClientAnalyticsResponseModel response = elasticQueryWebClient.getDataByText(requestModel);
        LOG.info("Query by text response: {}", response);
        model.addAttribute("elasticQueryWebClientResponseModels", response.getQueryResponseModels());
        model.addAttribute("wordCount", response.getWordCount());
        model.addAttribute("searchText", requestModel.getValue());
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        return "home";
    }
}
