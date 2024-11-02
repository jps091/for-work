package project.forwork;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = ForworkApplication.class)
@ActiveProfiles("test") // test 프로필을 활성화하여 application-test.yaml을 로드
@TestPropertySource(locations = "classpath:repository-custom-test.yaml")
class ForworkApplicationTests {

	@Test
	void contextLoads() {
	}

}
