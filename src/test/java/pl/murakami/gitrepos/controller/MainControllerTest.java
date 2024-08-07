package pl.murakami.gitrepos.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.murakami.gitrepos.model.*;
import pl.murakami.gitrepos.serivce.BranchesContentMapping;
import pl.murakami.gitrepos.serivce.UserReposDetailsService;
import pl.murakami.gitrepos.serivce.UserReposService;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import java.util.List;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MainControllerTest {

    @Mock
    UserReposDetailsService userReposDetailsService;
    @Mock
    UserReposService userReposService;
    @Mock
    BranchesContentMapping branchesContentMapping;
    @InjectMocks
    MainController controller;


    @Test
    @DisplayName("All user repos should be returned with reactive method")
    void getAllUserReposReactive_ReturnsAllUserRepos() {
        // given
        String username = "username";
        var userRepos = new UserRepos("Repository name", new UserData(username));
        doReturn(Flux.just(userRepos)).when(userReposService).getAllUserReposReactive(username);

        // when
        StepVerifier.create(this.controller.getAllUserReposReact(username))
                // then
                .expectNext(userRepos)
                .expectComplete()
                .verify();
        verify(this.userReposService).getAllUserReposReactive(username);
        verifyNoMoreInteractions(this.userReposService);
    }

    @Test
    @DisplayName("All user repos should be returned with virtual thread method")
    void getAllUserReposVirtual_ReturnsAllUserRepos() {
        // given
        String username = "username";
        var userRepos = new UserRepos("Repository name", new UserData(username));
        var completableFuture = CompletableFuture.completedFuture(List.of(userRepos));
        doReturn(completableFuture).when(userReposService).getAllUserReposAsync(username);

        // when
        CompletableFuture<List<UserRepos>> resultFuture = controller.getAllUserReposAsync(username);
        List<UserRepos> result = resultFuture.join();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Repository name", result.getFirst().getName());
        assertEquals(username, result.getFirst().getOwner().getLogin());
    }

    @Test
    @DisplayName("All user repos with details should be returned")
    void getAllUserReposDetails_ReturnsAllUserRepos() {
        // given
        String username = "username";
        String repoName = "TestRepo";
        String branchName = "TestBranch";
        String commitSha = "TestCommit";
        var userRepos = new UserRepos(repoName, new UserData(username));
        var userAndBranchesDataApiResponse = new UserAndBranchesDataApiResponse(userRepos, List.of(new BranchDetails(branchName, new CommitData(commitSha))));

        when(userReposDetailsService.getUserAndReposDetails(username)).thenReturn(List.of(userAndBranchesDataApiResponse));

        // when
        var response = controller.getAllOwnersRepos(username);

        // then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(repoName, response.getFirst().getUserRepos().getName());
        assertEquals(branchName, response.getFirst().getBranchDetails().getFirst().getName());
        assertEquals(commitSha, response.getFirst().getBranchDetails().getFirst().getCommit().getSha());
    }
}
