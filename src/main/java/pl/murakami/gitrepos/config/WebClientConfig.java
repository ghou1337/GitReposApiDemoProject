package pl.murakami.gitrepos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${gitApi.barerToken}")
    private String barerToken;
    @Value("${gitApi.acceptHeader}")
    private String acceptHeader;
    @Value("${gitApi.gitApiVersion}")
    private String gitApiVersion;
    @Value("${gitApi.base.url}")
    private String baseUrl;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader("Accept", acceptHeader)
                .defaultHeader("Authorization", barerToken)
                .defaultHeader("X-GitHub-Api-Version", gitApiVersion)
                .build();
    }
}
