package com.demo.microservice_2021.elastic.query.service.config;

import com.demo.microservice_2021.configdata.config.ElasticQueryServiceConfigData;
import com.demo.microservice_2021.configdata.config.UserConfigData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private final ElasticQueryServiceConfigData webclient;

    public WebClientConfig(ElasticQueryServiceConfigData webclient,
                           UserConfigData userConfigData) {
        this.webclient = webclient;
    }

    @LoadBalanced
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().doOnConnected(conn -> getTcpClient())))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(parseSizeToBytes(webclient.getWebclient().getMaxInMemorySize())));
    }

    private TcpClient getTcpClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webclient.getWebclient().getConnectionTimeoutMs())
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(webclient.getWebclient().getReadTimeoutMs(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(webclient.getWebclient().getWriteTimeoutMs(), TimeUnit.MILLISECONDS))
                )
                .wiretap(true);
    }

    private int parseSizeToBytes(String size) {
        if (size.toLowerCase().endsWith("mb")) {
            return Integer.parseInt(size.substring(0, size.length() - 2)) * 1024 * 1024;
        } else if (size.toLowerCase().endsWith("kb")) {
            return Integer.parseInt(size.substring(0, size.length() - 2)) * 1024;
        } else {
            return Integer.parseInt(size);
        }
    }
}
