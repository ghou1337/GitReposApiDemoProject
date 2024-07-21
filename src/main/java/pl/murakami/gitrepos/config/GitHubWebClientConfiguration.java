package pl.murakami.gitrepos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GitHubWebClientConfiguration {

    @Value("${gitApi.barerToken}")
    private String barerToken;
    @Value("${gitApi.acceptHeader}")
    private String acceptHeader;
    @Value("${gitApi.gitApiVersion}")
    private String gitApiVersion;

    @Bean
    public WebClient gitHubWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Accept", acceptHeader)
                .defaultHeader("Authorization", barerToken)
                .defaultHeader("X-GitHub-Api-Version", gitApiVersion)
                .build();
    }
}
