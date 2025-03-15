package com.demo.microservice_2021.elastic.query.web.client.config;

import com.demo.microservice_2021.configdata.config.ElasticQueryWebClientServiceConfigData;
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

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    private final ElasticQueryWebClientServiceConfigData.WebclientConfigData webclientConfigData;
    private final UserConfigData userConfigData;
    public WebClientConfig(ElasticQueryWebClientServiceConfigData.WebclientConfigData webclientConfigData,
                           UserConfigData userConfigData) {
        this.webclientConfigData = webclientConfigData;
        this.userConfigData = userConfigData;
    }

    @LoadBalanced
    @Bean("webClientBuilder")
    public WebClient.Builder webClientBuilder() {
        String auth = userConfigData.getUsername() + ":" + userConfigData.getPassword();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + encodedAuth;

        return WebClient.builder()
                .defaultHeaders(headers -> headers.set("Authorization", authHeader))
                .baseUrl(webclientConfigData.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().doOnConnected(conn -> getTcpClient())))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(webclientConfigData.getMaxInMemorySize()));
    }

    private TcpClient getTcpClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webclientConfigData.getConnectionTimeoutMs())
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(webclientConfigData.getReadTimeoutMs(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(webclientConfigData.getWriteTimeoutMs(), TimeUnit.MILLISECONDS))
                )
                .wiretap(true);
    }
}
