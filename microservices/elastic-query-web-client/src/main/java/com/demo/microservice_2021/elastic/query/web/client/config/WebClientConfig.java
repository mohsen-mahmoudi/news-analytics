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
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private final ElasticQueryWebClientServiceConfigData webclient;
    private final UserConfigData userConfigData;

    public WebClientConfig(ElasticQueryWebClientServiceConfigData webclient,
                           UserConfigData userConfigData) {
        this.webclient = webclient;
        this.userConfigData = userConfigData;
    }

    @LoadBalanced
    @Bean("webClientBuilder")
    public WebClient.Builder webClientBuilder(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                        authorizedClientManager);
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        oauth2.setDefaultClientRegistrationId("keycloak");

        String auth = userConfigData.getUsername() + ":" + userConfigData.getPassword();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        return WebClient.builder()
                .filter(oauth2)
                .baseUrl(webclient.getWebclient().getBaseUrl())
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
