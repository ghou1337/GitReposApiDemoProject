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
public class UseReposDetailsModel {
    private String repoName;
    private String username;
    private List<BranchDetailsDTO> branchDetails;
}
