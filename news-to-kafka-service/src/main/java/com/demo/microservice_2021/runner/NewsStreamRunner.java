package com.demo.microservice_2021.runner;

import com.demo.microservice_2021.config.NewsToKakfaServiceConfigData;
import com.demo.microservice_2021.dto.NewsRoot;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class NewsStreamRunner implements CommandLineRunner {

    private final WebClient webClient;
    private final NewsToKakfaServiceConfigData configData;

    public NewsStreamRunner(WebClient.Builder webClientBuilder, NewsToKakfaServiceConfigData configData) {
        this.webClient = webClientBuilder.baseUrl(configData.getNewsApiStreamUrl()).build();
        this.configData = configData;
    }

    public Flux<NewsRoot> streamNews(String filter) {
        return Flux.interval(Duration.ofSeconds(3600))
                .flatMap(tick -> fetchNews(filter))
                .onErrorContinue((error, obj) -> System.err.println("Error fetching news: " + error.getMessage()));
    }

    private Flux<NewsRoot> fetchNews(String filter) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", filter)
                        .queryParam("apiKey", configData.getNewsApiBearerToken())
                        .build())
                .retrieve()
                .bodyToFlux(NewsRoot.class);
    }

    @Override
    public void run(String... args) throws Exception {
        fetchNews(configData.getNewsKeyword()).subscribe(news -> {
            news.getArticles().stream().forEach(article -> {
                try {
                    System.out.println("Processing: " + article.getContent());
                    Thread.sleep(1000); // 1-second delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
        });
    }
}
