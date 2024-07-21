package pl.murakami.gitrepos.serivce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.murakami.gitrepos.config.GitHubWebClientConfiguration;
import pl.murakami.gitrepos.model.UserRepos;
import pl.murakami.gitrepos.exe.UserNotFoundException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class UserReposService {
    @Autowired
    private GitHubWebClientConfiguration gitHubWebClientConfiguration;

    // getting user data and all his repositories
    public List<UserRepos> getAllUserRepos(String username) {
        WebClient webClient = gitHubWebClientConfiguration.gitHubWebClient();
        try {
            return webClient.get()
                .uri("/users/{username}/repos", username)
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(UserRepos.class)
                .collectList()
                .block();
        } catch (WebClientResponseException e) {
           throw new UserNotFoundException(e.getMessage(), e.getStatusCode()); // error handling in case the user does not find
        }
    }
}
