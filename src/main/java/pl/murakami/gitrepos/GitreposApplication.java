package pl.murakami.gitrepos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GitreposApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitreposApplication.class, args);
	}

}
