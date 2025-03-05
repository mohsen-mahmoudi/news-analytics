package com.demo.microservice_2021.newsToKafka.service.impl;

import com.demo.microservice_2021.configdata.config.KafkaConfigData;
import com.demo.microservice_2021.kafka.avro.model.NewsAvroModel;
import com.demo.microservice_2021.newsToKafka.dto.Article;
import com.demo.microservice_2021.newsToKafka.service.NewsToKafkaService;
import com.demo.microservice_2021.newsToKafka.transformer.NewsArticleToAvroTransformer;
import com.demo.microservice_2021.producer.service.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NewsToKafkaServiceImpl implements NewsToKafkaService {

    private static final Logger LOG = LoggerFactory.getLogger(NewsToKafkaServiceImpl.class);

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<Long, NewsAvroModel> kafkaProducer;
    private final NewsArticleToAvroTransformer newsArticleToAvroTransformer;

    public NewsToKafkaServiceImpl(KafkaConfigData kafkaConfigData, KafkaProducer<Long, NewsAvroModel> kafkaProducer,
                                  NewsArticleToAvroTransformer newsArticleToAvroTransformer) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducer = kafkaProducer;
        this.newsArticleToAvroTransformer = newsArticleToAvroTransformer;
    }

    @Override
    public void streamNewsToKafka(Article article) {
        try {
            LOG.info("Sending News to Kafka : {}", article);
            NewsAvroModel newsAvroModel = newsArticleToAvroTransformer.getNewsAvroModelFromStatus(article);
            kafkaProducer.sendMessage(kafkaConfigData.getTopicName(), newsAvroModel.getUserId(), newsAvroModel);
        } catch (Exception e) {
            LOG.info("Error sending news to kafka : {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
