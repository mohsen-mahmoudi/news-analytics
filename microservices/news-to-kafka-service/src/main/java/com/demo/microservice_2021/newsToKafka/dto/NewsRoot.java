package com.demo.microservice_2021.newsToKafka.dto;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@ToString
@Data
public class NewsRoot {
    public String status;
    public int totalResults;
    public ArrayList<Article> articles = new ArrayList<>();
}
