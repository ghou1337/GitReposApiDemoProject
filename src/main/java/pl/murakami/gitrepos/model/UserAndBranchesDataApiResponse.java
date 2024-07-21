package pl.murakami.gitrepos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAndBranchesDataApiResponse {
    private UserRepos userRepos;
    private List<BranchDetails> branchDetails;

    public UserAndBranchesDataApiResponse(UserRepos userRepos, List<BranchDetails> branchDetails) {
        this.userRepos = userRepos;
        this.branchDetails = branchDetails;
    }
}
