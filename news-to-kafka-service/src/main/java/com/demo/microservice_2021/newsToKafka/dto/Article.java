package com.demo.microservice_2021.newsToKafka.dto;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Article {
    public Source source;
    public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    public String publishedAt;
    public String content;
}