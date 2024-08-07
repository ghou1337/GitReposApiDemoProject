package pl.murakami.gitrepos.exe;

import org.springframework.http.HttpStatusCode;


public record UserNotFoundResponse(HttpStatusCode status, String message) {
}
