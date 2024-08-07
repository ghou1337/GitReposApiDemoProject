package pl.murakami.gitrepos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.murakami.gitrepos.model.UserAndBranchesDataApiResponse;
import pl.murakami.gitrepos.exe.UserNotFoundException;
import pl.murakami.gitrepos.exe.UserNotFoundResponse;
import pl.murakami.gitrepos.model.UserRepos;
import pl.murakami.gitrepos.serivce.UserReposDetailsService;
import pl.murakami.gitrepos.serivce.UserReposService;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {
    private final UserReposDetailsService userReposDetailsService;
    private final UserReposService userReposService;

    @GetMapping("/repos-details")
    public List<UserAndBranchesDataApiResponse> getAllOwnersRepos(@RequestParam("username") String username) {
        return userReposDetailsService.getUserAndReposDetails(username);
    }

    @GetMapping("/repos")
    public List<UserRepos> getAllUserRepos(@RequestParam("username") String username) {
        return userReposService.getAllUserRepos(username);
    }

    @GetMapping("/repos-reactor")
    public Flux<UserRepos> getAllUserReposReact(@RequestParam("username") String username) {
        return userReposService.getAllUserReposReactive(username);
    }

    @GetMapping("/repos-async")
    public CompletableFuture<List<UserRepos>> getAllUserReposAsync(@RequestParam("username")  String username) {
        return userReposService.getAllUserReposAsync(username);
    }
    // implementation of the api response in case of an error
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserNotFoundResponse> handException(UserNotFoundException e) {
        UserNotFoundResponse userNotFoundResponse = new UserNotFoundResponse(
                e.getHttpStatus(), e.getMessage());

        return new ResponseEntity<>(userNotFoundResponse, e.getHttpStatus());
    }

//    @ExceptionHandler(BadRequestException.class)
//    public ResponseEntity<UserNotFoundResponse> handException(BadRequestException e) {
//        UserNotFoundResponse userNotFoundResponse = new UserNotFoundResponse(
//                HttpStatus.BAD_REQUEST, e.getMessage());
//
//        return new ResponseEntity<>(userNotFoundResponse, HttpStatus.BAD_REQUEST
//        );
//    }
}
