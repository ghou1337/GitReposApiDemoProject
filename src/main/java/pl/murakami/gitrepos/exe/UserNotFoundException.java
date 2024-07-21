package pl.murakami.gitrepos.exe;

import org.springframework.http.HttpStatusCode;

public class UserNotFoundException extends RuntimeException{

    private final HttpStatusCode httpStatus;
    public UserNotFoundException(String message, HttpStatusCode httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatusCode getHttpStatus() {
        return httpStatus;
    }
}
