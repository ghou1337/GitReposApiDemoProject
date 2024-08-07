package pl.murakami.gitrepos.serivce;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.murakami.gitrepos.model.UserRepos;
import pl.murakami.gitrepos.exe.UserNotFoundException;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class UserReposService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private WebClient webClient;

    public List<UserRepos> getAllUserRepos(String username) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<List<UserRepos>> future = executor.submit(() -> {
                try {
                    return restClient.get()
                            .uri("/users/{username}/repos", username)
                            .accept(APPLICATION_JSON)
                            .retrieve()
                            .body(new ParameterizedTypeReference<>() {});
                } catch (HttpClientErrorException e) {
                    if(e.getStatusCode() == HttpStatus.NOT_FOUND)
                        throw new UserNotFoundException(e.getMessage(), e.getStatusCode()); // error handling in case the user is not found
                    if(e.getStatusCode() == HttpStatus.BAD_REQUEST)
                        throw new BadRequestException(e.getMessage());
                    else
                        throw new HttpClientErrorException(e.getStatusCode());
                }
            });
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("Error executing request in virtual thread", e);
        }
    }

    // getting user data and all his repositories
    public Flux<UserRepos> getAllUserReposReactive(String username) {
        try {
            return webClient.get()
                    .uri("/users/{username}/repos", username)
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(UserRepos.class);
        } catch (WebClientResponseException e) {
            throw new UserNotFoundException(e.getMessage(), e.getStatusCode()); // error handling in case the user does not find
        }
    }

    @Async("virtualThreadExecutor")
    public CompletableFuture<List<UserRepos>> getAllUserReposAsync(String username) {
        try {
            List<UserRepos> result = restClient
                    .get()
                    .uri("/users/{username}/repos", username)
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return CompletableFuture.completedFuture(result);
        } catch (WebClientResponseException e) {
            throw new UserNotFoundException(e.getMessage(), e.getStatusCode()); // error handling in case the user is not found
        }
    }
}
