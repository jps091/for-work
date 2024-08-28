package project.forwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ForworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForworkApplication.class, args);
	}

}
