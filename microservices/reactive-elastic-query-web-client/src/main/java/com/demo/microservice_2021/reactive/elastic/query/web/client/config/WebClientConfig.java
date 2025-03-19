package com.demo.microservice_2021.reactive.elastic.query.web.client.config;

import com.demo.microservice_2021.configdata.config.ElasticQueryWebClientServiceConfigData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private final ElasticQueryWebClientServiceConfigData configData;

    public WebClientConfig(ElasticQueryWebClientServiceConfigData configData) {
        this.configData = configData;
    }

    @Bean("webClient")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(configData.getWebclient().getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().doOnConnected(conn -> getTcpClient())))
                .build();
    }

    private TcpClient getTcpClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, configData.getWebclient().getConnectionTimeoutMs())
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(configData.getWebclient().getReadTimeoutMs(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(configData.getWebclient().getWriteTimeoutMs(), TimeUnit.MILLISECONDS))
                )
                .wiretap(true);
    }
}
