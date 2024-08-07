package pl.murakami.gitrepos.serivce;

import org.springframework.stereotype.Component;
import pl.murakami.gitrepos.model.BranchDetails;
import pl.murakami.gitrepos.model.UserAndBranchesDataApiResponse;
import pl.murakami.gitrepos.model.UserRepos;

import java.util.List;

@Component
public class BranchesContentMapping extends UserBranchesContentService{
    // getting branches and commits for repositories
    public UserAndBranchesDataApiResponse mapToAllUserReposDetails(UserRepos repo) {
        List<BranchDetails> branchDetails = getReposDetails(repo);
        return new UserAndBranchesDataApiResponse(repo, branchDetails);
    }
}
