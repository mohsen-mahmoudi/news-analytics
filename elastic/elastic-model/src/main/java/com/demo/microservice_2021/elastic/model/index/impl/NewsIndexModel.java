package com.demo.microservice_2021.elastic.model.index.impl;

import com.demo.microservice_2021.elastic.model.index.IndexModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@Data
@Builder
@Document(indexName = "${elastic-config.index-name}")
public class NewsIndexModel implements IndexModel {

    @JsonProperty
    private Long id;
    @JsonProperty
    private Long userId;
    @JsonProperty
    private String text;
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    @JsonProperty
    private LocalDateTime createdAt;

}
