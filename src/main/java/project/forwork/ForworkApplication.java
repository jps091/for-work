package project.forwork;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableRetry
public class ForworkApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ForworkApplication.class)
				.profiles("prod")
				.run(args);
	}
}
