package com.demo.microservice_2021.newsToKafka.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Source {
    public String id;
    public String name;
}