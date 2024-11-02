package project.forwork.api.domain.resume.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import project.forwork.api.domain.resume.controller.model.ResumeAdminResponse;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.mock.TestClockHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:repository-custom-test.yaml")
@DataJpaTest
@Import({ResumeRepositoryCustomImpl.class})
@SqlGroup({
        @Sql(value = "/sql/user-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/resume-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class ResumeRepositoryCustomImplTest {

    @Autowired
    private ResumeRepositoryCustomImpl repository;

    @TestConfiguration
    static class TestClockHolderConfig{
        @Bean
        public TestClockHolder testClockHolder(){
            return TestClockHolder.builder()
                    .localDate(LocalDate.of(2024, 9, 10))
                    .localDateTime(LocalDateTime.of(2024, 9, 10, 23, 58))
                    .build();
        }
    }

    @Test
    void 이력서_첫페이지_조건X_검색() {
        //given(상황환경 세팅
        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findFirstPage(null, null, 2);

        //then(검증)
        assertThat(result).hasSize(2);
    }
    @Test
    void 이력서_첫페이지_조건_TODAY_검색() {
        //given(상황환경 세팅)
        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findFirstPage(PeriodCond.TODAY, null, 2);

        //then(검증)
        assertThat(result).hasSize(1);
    }

    @Test
    void 이력서_첫페이지_조건_WEEK_검색() {
        //given(상황환경 세팅)

        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findFirstPage(PeriodCond.WEEK, null, 10);

        //then(검증)
        assertThat(result).hasSize(6);
    }

    @Test
    void 이력서_첫페이지_조건_WEEK_ACTIVE_검색() {
        //given(상황환경 세팅)
        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findFirstPage(PeriodCond.WEEK, ResumeStatus.ACTIVE, 10);

        //then(검증)
        assertThat(result).hasSize(5);
    }

    @Test
    void 이력서_첫페이지_조건_MONTH_PENDING_검색() {
        //given(상황환경 세팅)
        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findFirstPage(PeriodCond.MONTH, ResumeStatus.PENDING, 10);

        //then(검증)
        assertThat(result).hasSize(2);
    }

    @Test
    void 이력서_다음페이지_요청시간이_같다면_ID_값이_낮은게_먼저_반환_된다() {
        //given(상황환경 세팅)
        //2024-09-04 00:18:39
        LocalDateTime last = LocalDateTime.of(2024, 9, 5, 0, 18, 39);

        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findNextPage(null, null, last, 4L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(5L);
        assertThat(result.get(0).getModifiedAt()).isEqualTo(last);
    }


    @Test
    void 이력서_다음페이지_조건_WEEK() {
        //given(상황환경 세팅) 2024-09-04 00:18:39
        LocalDateTime last = LocalDateTime.of(2024, 9, 4, 0, 18, 39);

        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findNextPage(PeriodCond.WEEK, null, last, 3L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(4L);
        assertThat(result.get(1).getId()).isEqualTo(5L);
    }

    @Test
    void 이력서_다음페이지_조건_WEEK_PENDING() {
        //given(상황환경 세팅) 2024-09-04 00:18:39
        LocalDateTime last = LocalDateTime.of(2024, 9, 4, 0, 18, 39);

        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findNextPage(PeriodCond.WEEK, ResumeStatus.PENDING, last, 3L, 2);

        //then(검증)
        assertThat(result).hasSize(0);
    }

    @Test
    void 이력서_다음페이지_조건_MONTH_ACTIVE() {
        //given(상황환경 세팅) 2024-09-05 00:18:39
        LocalDateTime last = LocalDateTime.of(2024, 9, 5, 0, 18, 39);

        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findNextPage(PeriodCond.MONTH, ResumeStatus.ACTIVE, last, 5L, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(7L);
        assertThat(result.get(1).getId()).isEqualTo(8L);
        assertThat(result.get(2).getId()).isEqualTo(6L);
    }

    @Test
    void 이력서_이전_페이지_조건_WEEK_ACTIVE_검색() {
        //given(상황환경 세팅)
        LocalDateTime last = LocalDateTime.of(2024, 9, 7, 0, 18, 39);

        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findPreviousPage(PeriodCond.WEEK, ResumeStatus.ACTIVE, last, 7L, 2);

        //then(검증)
        assertThat(result.get(0).getId()).isEqualTo(4L);
        assertThat(result.get(1).getId()).isEqualTo(5L);
    }

    @Test
    void 이력서_이전_페이지_조건_MONTH_PENDING_검색() {
        //given(상황환경 세팅)
        LocalDateTime last = LocalDateTime.of(2024, 9, 4, 0, 18, 39);

        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findPreviousPage(PeriodCond.MONTH, ResumeStatus.PENDING, last, 3L, 2);

        //then(검증)
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void 이력서_마지막_페이지_조건X_검색() {
        //given(상황환경 세팅)
        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findLastPage(null, null, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(8L);
        assertThat(result.get(1).getId()).isEqualTo(6L);
    }

    @Test
    void 이력서_마지막_페이지_조건_WEEK_ACTIVE_검색() {
        //given(상황환경 세팅)
        //when(상황발생)
        List<ResumeAdminResponse> result = repository.findLastPage(PeriodCond.WEEK, ResumeStatus.ACTIVE, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(8L);
        assertThat(result.get(1).getId()).isEqualTo(6L);
    }
}