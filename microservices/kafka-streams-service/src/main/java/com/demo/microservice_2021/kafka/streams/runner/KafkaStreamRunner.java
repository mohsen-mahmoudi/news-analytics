package com.demo.microservice_2021.kafka.streams.runner;

import com.demo.microservice_2021.configdata.config.KafkaConfigData;
import com.demo.microservice_2021.configdata.config.KafkaStreamsConfigData;
import com.demo.microservice_2021.kafka.avro.model.NewsAnalyticsAvroModel;
import com.demo.microservice_2021.kafka.avro.model.NewsAvroModel;
import com.demo.microservice_2021.kafka.streams.init.StreamInitializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

@Component
public class KafkaStreamRunner implements StreamRunner {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamRunner.class);
    private static final String REGEX = "\\W+";

    private final StreamInitializer streamInitializer;
    private final KafkaConfigData kafkaConfigData;
    private final KafkaStreamsConfigData kafkaStreamsConfigData;
    private final Properties kafkaStreamsConfigs;

    private KafkaStreams kafkaStreams;
    private volatile ReadOnlyKeyValueStore<String, Long> keyValueStore;

    public KafkaStreamRunner(StreamInitializer streamInitializer,
                             KafkaConfigData kafkaConfigData,
                             KafkaStreamsConfigData kafkaStreamsConfigData,
                             @Qualifier("kafkaStreamsConfigs") Properties kafkaStreamsConfigs) {
        this.streamInitializer = streamInitializer;
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaStreamsConfigData = kafkaStreamsConfigData;
        this.kafkaStreamsConfigs = kafkaStreamsConfigs;
    }

    @Override
    public void run(String... args) throws Exception {
        streamInitializer.init();
        final Map<String, String> serdeConfig = Collections.singletonMap(
                kafkaConfigData.getSchemaRegistryUrlKey(),
                kafkaConfigData.getSchemaRegistryUrl()
        );
        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<Long, NewsAvroModel> stream = getNewsAvroModelKStream(serdeConfig, streamsBuilder);
        createTopology(stream, serdeConfig);
        final Topology topology = streamsBuilder.build();
        LOG.info("Defined topology: {}", topology.describe());
        kafkaStreams = new KafkaStreams(topology, kafkaStreamsConfigs);
        kafkaStreams.start();
    }

    @PreDestroy
    public void close() {
        if (kafkaStreams != null) {
            kafkaStreams.close();
            LOG.info("Kafka streams closed");
        }
    }

    private void createTopology(KStream<Long, NewsAvroModel> stream, Map<String, String> serdeConfig) {
        Pattern pattern = Pattern.compile(REGEX, Pattern.UNICODE_CHARACTER_CLASS);
        Serde<NewsAnalyticsAvroModel> newsAnalyticsAvroModelSerde = getSerdeAnalyticsModel(serdeConfig);
        stream
                .flatMapValues(value -> Arrays.asList(pattern.split(value.getText().toLowerCase())))
                .groupBy((key, word) -> word)
                .count(Materialized.as(kafkaStreamsConfigData.getWordCountStoreName()))
                .toStream()
                .map(mapToAnalyticsModel())
                .to(kafkaStreamsConfigData.getOutputTopicName(),
                        Produced.with(Serdes.String(), newsAnalyticsAvroModelSerde));
    }

    private KeyValueMapper<String, Long, KeyValue<String, NewsAnalyticsAvroModel>> mapToAnalyticsModel() {
        return (word, count) -> {
            LOG.info("Sending to topic {}, word {}, count {}", kafkaStreamsConfigData.getOutputTopicName(), word, count);
            return new KeyValue<>(word, NewsAnalyticsAvroModel.newBuilder()
                    .setWord(word)
                    .setWordCount(count)
                    .setCreatedAt(LocalDate.now().toEpochSecond(LocalTime.now(), ZoneOffset.UTC))
                    .build());
        };
    }

    private Serde<NewsAnalyticsAvroModel> getSerdeAnalyticsModel(Map<String, String> serdeConfig) {
        final Serde<NewsAnalyticsAvroModel> newsAnalyticsAvroModelSerde = new SpecificAvroSerde<>();
        newsAnalyticsAvroModelSerde.configure(serdeConfig, false);
        return newsAnalyticsAvroModelSerde;
    }

    private KStream<Long, NewsAvroModel> getNewsAvroModelKStream(Map<String, String> serdeConfig, StreamsBuilder streamsBuilder) {
        final Serde<NewsAvroModel> newsAvroModelSerde = new SpecificAvroSerde<>();
        newsAvroModelSerde.configure(serdeConfig, false);
        KStream<Long, NewsAvroModel> stream = streamsBuilder.stream(kafkaConfigData.getTopicName(), Consumed.with(Serdes.Long(),
                newsAvroModelSerde));
        return stream;
    }

    @Override
    public Long getValueByKey(String word) {
        if(kafkaStreams != null && kafkaStreams.state() == KafkaStreams.State.RUNNING) {
            if(keyValueStore == null) {
                synchronized (this) {
                    if(keyValueStore == null) {
                        keyValueStore = kafkaStreams.store(StoreQueryParameters
                                .fromNameAndType(kafkaStreamsConfigData.getWordCountStoreName(),
                                        QueryableStoreTypes.keyValueStore()));
                    }
                }
            }
            return keyValueStore.get(word.toLowerCase());
        }
        return 0L;
    }
}
