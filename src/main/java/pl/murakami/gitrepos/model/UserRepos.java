package pl.murakami.gitrepos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
    public class UserRepos {
    private String name; // user repositories name
    private UserData owner; // user data object
}
