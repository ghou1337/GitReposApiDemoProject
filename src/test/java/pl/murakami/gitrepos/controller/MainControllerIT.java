package pl.murakami.gitrepos.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.murakami.gitrepos.model.UserAndBranchesDataApiResponse;
import pl.murakami.gitrepos.model.UserRepos;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
public class MainControllerIT {

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();
    @Autowired
    private WebTestClient webTestClient;

    @DynamicPropertySource
    public static void setUpMockServer(DynamicPropertyRegistry registry) {
        registry.add("gitApi.base.url", wireMockExtension::baseUrl);
    }

    @BeforeEach
    void setUp() {
        // given
        String userRepos = """
                [
                    {
                        "name": "repos-name",
                        "owner": {
                            "login": "user123"
                        }
                    }
                ]
                """;

        String userReposDetails = """
                [
                    {
                        "name": "commit1",
                        "commit": {
                            "sha": "c1ebead1234567890c1ebead12345"
                        }
                    }
                ]
                """;

        wireMockExtension.stubFor(
                WireMock.get("/users/user123/repos")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(userRepos)
                        ));
        wireMockExtension.stubFor(
                WireMock.get("/repos/user123/repos-name/branches")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(userReposDetails)
                        ));
    }

    @AfterEach
    void tearDown() {
        wireMockExtension.resetAll();
    }

    @Test
    void getAllUserRepos_ReturnsAllUserRepos() {
        // when
        List<UserRepos> response = webTestClient.get().uri(UriBuilder -> UriBuilder
                        .path("/api/repos")
                        .queryParam("username", "user123")
                        .build())
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<UserRepos>>() {})
                .returnResult()
                .getResponseBody();
        assertNotNull(response);
        assertEquals("repos-name", response.getFirst().getName());
        assertEquals("user123", response.getFirst().getOwner().getLogin());
        assertEquals(1, response.size());
    }

    @Test
    void getUserRepos_UserNotFound_ReturnsUserNotFound() {
        //when
        webTestClient.get().uri(UriBuilder -> UriBuilder
                .path("/api/repos")
                .queryParam("username", "NotExitingUser")
                .build())
            .exchange()
            //then
            .expectStatus().isNotFound()
            .expectBody()
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isNotEmpty();
    }

    @Test
    void getAllOwnersRepos_ReturnsAllOwnerRepos() {
        //when
        List<UserAndBranchesDataApiResponse> responses = webTestClient.get().uri(UriBuilder -> UriBuilder
                .path("/api/repos-details")
                .queryParam("username", "user123")
                .build())
                .exchange()
            //then
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<UserAndBranchesDataApiResponse>> () {})
            .returnResult()
            .getResponseBody();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("repos-name", responses.getFirst().getUserRepos().getName());
        assertEquals("user123", responses.getFirst().getUserRepos().getOwner().getLogin());
        assertEquals("c1ebead1234567890c1ebead12345", responses.getFirst().getBranchDetails().getFirst().getCommit().getSha());
    }
}
