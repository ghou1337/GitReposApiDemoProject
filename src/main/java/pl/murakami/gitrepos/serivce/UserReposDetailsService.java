package pl.murakami.gitrepos.serivce;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.murakami.gitrepos.model.UserAndBranchesDataApiResponse;
import pl.murakami.gitrepos.model.BranchDetails;
import pl.murakami.gitrepos.model.UserRepos;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserReposDetailsService {
    private final UserBranchesContentService userBranchContentService;
    private final UserReposService userReposService;

    // creating a list with (user; repos) and (branches; commits) data
    public List<UserAndBranchesDataApiResponse> getUserAndReposDetails(String username) {
        List<UserRepos> userReposList = userReposService.getAllUserRepos(username);

        return userReposList.stream()
                .map(this::mapToAllUserReposDetails)
                .collect(Collectors.toList());
    }

    // getting branches and commits for repositories
    private UserAndBranchesDataApiResponse mapToAllUserReposDetails(UserRepos repo) {
        List<BranchDetails> branchDetails = userBranchContentService.getReposDetails(repo);
        return new UserAndBranchesDataApiResponse(repo, branchDetails);
    }
}
