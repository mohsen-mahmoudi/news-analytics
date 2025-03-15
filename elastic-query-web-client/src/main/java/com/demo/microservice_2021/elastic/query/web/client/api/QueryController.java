package com.demo.microservice_2021.elastic.query.web.client.api;

import com.demo.microservice_2021.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.demo.microservice_2021.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QueryController {
    private static final Logger LOG = LoggerFactory.getLogger(QueryController.class);

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

    @PostMapping("query-by-text")
    public String queryByText(@Valid ElasticQueryWebClientRequestModel requestModel, Model model) {
        LOG.info("Query by text requested: {}", requestModel);

        List<ElasticQueryWebClientResponseModel> response = new ArrayList<>();
        response.add(ElasticQueryWebClientResponseModel.builder()
                .id("1").userId("userid").text("text1").createdAt(LocalDateTime.now()).build());

        model.addAttribute("elasticQueryWebClientResponseModels", response);
        model.addAttribute("searchText", requestModel.getText());
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        return "home";
    }
}
