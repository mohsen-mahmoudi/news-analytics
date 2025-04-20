package com.demo.microservice_2021.elastic.query.web.client.common.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ElasticQueryWebClientAnalyticsResponseModel {
    private List<ElasticQueryWebClientResponseModel> queryResponseModel;
    private Long wordCount;
}
