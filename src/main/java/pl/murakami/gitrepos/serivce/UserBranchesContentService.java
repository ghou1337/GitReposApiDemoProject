package pl.murakami.gitrepos.serivce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.murakami.gitrepos.config.GitHubWebClientConfiguration;
import pl.murakami.gitrepos.model.BranchDetails;
import pl.murakami.gitrepos.model.UserRepos;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class UserBranchesContentService {
    @Autowired
    private GitHubWebClientConfiguration gitHubWebClientConfiguration;

    // returns a list of all branches and the last commit of the user repository
    public List<BranchDetails> getReposDetails(UserRepos userRepos) {
        WebClient webClient = gitHubWebClientConfiguration.gitHubWebClient();
        try {
            return webClient.get()
                .uri("/repos/{owner}/{repo}/branches", userRepos.getOwner().getLogin(), userRepos.getName())
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(BranchDetails.class)
                .collectList()
                .block();
        } catch (WebClientResponseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
