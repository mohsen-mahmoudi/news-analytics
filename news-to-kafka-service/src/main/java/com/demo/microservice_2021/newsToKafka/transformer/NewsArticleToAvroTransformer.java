package com.demo.microservice_2021.newsToKafka.transformer;

import com.demo.microservice_2021.kafka.avro.model.NewsAvroModel;
import com.demo.microservice_2021.newsToKafka.dto.Article;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class NewsArticleToAvroTransformer {

    public NewsAvroModel getNewsAvroModelFromStatus(Article article) {
        return NewsAvroModel.newBuilder()
                .setId(new Random().nextInt(1000000))
                .setUserId(article.getSource().getName().hashCode())
                .setCreatedAt(System.currentTimeMillis())
                .setText(article.getTitle())
                .build();
    }
}
