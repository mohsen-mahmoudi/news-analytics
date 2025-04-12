package com.demo.microservice_2021.kafka.streams.init;

import com.demo.microservice_2021.configdata.config.KafkaConfigData;
import com.demo.microservice_2021.kafak.admin.client.KafkaAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StreamInitializerImpl implements StreamInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(StreamInitializerImpl.class);

    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    public StreamInitializerImpl(KafkaConfigData kafkaConfigData, KafkaAdminClient kafkaAdminClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @Override
    public void init() {
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistry();
        LOG.info("Kafka Stream initialization complete, Topic Names : {} is already created",
                kafkaConfigData.getTopicNamesToCreate().toString());
    }
}
