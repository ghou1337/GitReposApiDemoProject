package pl.murakami.gitrepos.serivce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.murakami.gitrepos.model.BranchDetails;
import pl.murakami.gitrepos.model.UserRepos;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class UserBranchesContentService {
    @Autowired
    private RestClient restClient;

    // returns a list of all branches and the last commit of the user repository
    public List<BranchDetails> getReposDetails(UserRepos userRepos) {
        try(var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<List<BranchDetails>> future = executor.submit(() -> {
                try {
                    return restClient.get()
                            .uri("/repos/{owner}/{repo}/branches", userRepos.getOwner().getLogin(), userRepos.getName())
                            .accept(APPLICATION_JSON)
                            .retrieve()
                            .body(new ParameterizedTypeReference<>() {});
                } catch (WebClientResponseException e) {
                    e.getMessage();
                    return null;
                }
            });
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("Error executing request in virtual thread", e);
        }
    }
}
