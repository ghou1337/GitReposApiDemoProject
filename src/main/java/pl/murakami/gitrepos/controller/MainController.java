package pl.murakami.gitrepos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.murakami.gitrepos.model.UserAndBranchesDataApiResponse;
import pl.murakami.gitrepos.exe.UserNotFoundException;
import pl.murakami.gitrepos.exe.UserNotFoundResponse;
import pl.murakami.gitrepos.serivce.UserReposDetailsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/repos")
public class MainController {
    private final UserReposDetailsService userReposDetailsService;

    @GetMapping("/get-all")
    public List<UserAndBranchesDataApiResponse> getAllOwnersRepos(@RequestParam("username") String username) {
        return userReposDetailsService.getUserAndReposDetails(username);
    }

    // implementation of the api response in case of an error
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserNotFoundResponse> handException(UserNotFoundException e) {
        UserNotFoundResponse userNotFoundResponse = new UserNotFoundResponse(
                e.getHttpStatus(), e.getMessage());

        return new ResponseEntity<>(userNotFoundResponse, e.getHttpStatus());
    }
}
