package project.forwork.api.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import project.forwork.api.mock.TestClockHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;


@TestConfiguration
public class TestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public TestClockHolder testClockHolder(){
        TestClockHolder clockHolder = TestClockHolder.builder()
                .localDate(LocalDate.of(2024, 9, 10))
                .localDateTime(LocalDateTime.of(2024, 9, 10, 23, 58))
                .build();
        return clockHolder;
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
