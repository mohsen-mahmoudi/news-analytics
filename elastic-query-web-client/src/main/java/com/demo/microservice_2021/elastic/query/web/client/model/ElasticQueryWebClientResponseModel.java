package com.demo.microservice_2021.elastic.query.web.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryWebClientResponseModel {
    private String id;
    private String userId;
    private String text;
    private LocalDateTime createdAt;
}
