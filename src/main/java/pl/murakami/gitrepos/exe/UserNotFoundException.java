package pl.murakami.gitrepos.exe;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final HttpStatusCode httpStatus;
    public UserNotFoundException(String message, HttpStatusCode httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
