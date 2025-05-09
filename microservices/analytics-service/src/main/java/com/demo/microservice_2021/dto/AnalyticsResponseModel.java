package com.demo.microservice_2021.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponseModel {
    private UUID id;
    private String word;
    private Long wordCount;
}
