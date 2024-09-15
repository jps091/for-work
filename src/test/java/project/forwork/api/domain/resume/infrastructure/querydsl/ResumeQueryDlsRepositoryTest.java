package project.forwork.api.domain.resume.infrastructure.querydsl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import project.forwork.api.domain.resume.controller.model.ResumeResponse;
import project.forwork.api.domain.resume.infrastructure.ResumeQueryDlsRepository;
import project.forwork.api.domain.resume.infrastructure.ResumeSearchCond;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource("classpath:test-application.yml")
@SqlGroup({
        @Sql(value = "/sql/resume-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class ResumeQueryDlsRepositoryTest {

    @Autowired
    private ResumeQueryDlsRepository repository;

    /***
     * ('test1', 'FRONTEND', 'NEW', 'PENDING')
     * ('test2', 'AI', 'SENIOR', 'REJECTED')
     * ('test3', 'BACKEND', 'JUNIOR', 'PENDING')
     * ('test4', 'FRONTEND', 'NEW', 'ACTIVE')
     */
    @Test
    void 이력서_조건X_검색() {
        // Given: 검색 조건을 설정
        ResumeSearchCond cond = new ResumeSearchCond();

        // 페이징 설정 (0번 페이지, 10개씩 가져옴)
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When: search 메서드를 호출하여 쿼리 실행
        Page<ResumeResponse> result = repository.search(cond, pageRequest);
        List<ResumeResponse> content = result.getContent();

        // Then: 결과 검증
        assertThat(content).hasSize(8);
    }

    @Test
    void 이력서_필드조건_검색() {
        // Given: 검색 조건을 설정
        ResumeSearchCond cond = new ResumeSearchCond();
        cond.setField(FieldType.FRONTEND);

        PageRequest pageRequest = PageRequest.of(0, 10);

        // When: search 메서드를 호출하여 쿼리 실행
        Page<ResumeResponse> result = repository.search(cond, pageRequest);
        List<ResumeResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(resume -> resume.getField().equals(FieldType.FRONTEND));
    }

    @Test
    void 이력서_레벨조건_검색() {
        // Given: 검색 조건을 설정
        ResumeSearchCond cond = new ResumeSearchCond();
        cond.setLevel(LevelType.NEW);

        // 페이징 설정 (0번 페이지, 10개씩 가져옴)
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When: search 메서드를 호출하여 쿼리 실행
        Page<ResumeResponse> result = repository.search(cond, pageRequest);
        List<ResumeResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(resume -> resume.getLevel().equals(LevelType.NEW));
    }

    @Test
    void 이력서_상태조건_검색() {
        // Given: 검색 조건을 설정
        ResumeSearchCond cond = new ResumeSearchCond();
        cond.setResumeStatus(ResumeStatus.ACTIVE);

        // 페이징 설정 (0번 페이지, 10개씩 가져옴)
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When: search 메서드를 호출하여 쿼리 실행
        Page<ResumeResponse> result = repository.search(cond, pageRequest);
        List<ResumeResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(resume -> resume.getStatus().equals(ResumeStatus.ACTIVE));
    }

    @Test
    void 이력서_상태_필드_조건_검색() {
        // Given: 검색 조건을 설정
        ResumeSearchCond cond = new ResumeSearchCond();
        cond.setResumeStatus(ResumeStatus.PENDING);
        cond.setField(FieldType.BACKEND);

        // 페이징 설정 (0번 페이지, 10개씩 가져옴)
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When: search 메서드를 호출하여 쿼리 실행
        Page<ResumeResponse> result = repository.search(cond, pageRequest);
        List<ResumeResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(resume -> resume.getStatus().equals(ResumeStatus.PENDING));
        assertThat(content).allMatch(resume -> resume.getField().equals(FieldType.BACKEND));
    }

    @Test
    void 이력서_상태_필드_레벨_조건_검색() {
        // Given: 검색 조건을 설정
        ResumeSearchCond cond = new ResumeSearchCond();
        cond.setResumeStatus(ResumeStatus.REJECTED);
        cond.setField(FieldType.AI);
        cond.setLevel(LevelType.SENIOR);

        // 페이징 설정 (0번 페이지, 10개씩 가져옴)
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When: search 메서드를 호출하여 쿼리 실행
        Page<ResumeResponse> result = repository.search(cond, pageRequest);
        List<ResumeResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(resume -> resume.getStatus().equals(ResumeStatus.REJECTED));
        assertThat(content).allMatch(resume -> resume.getField().equals(FieldType.AI));
        assertThat(content).allMatch(resume -> resume.getLevel().equals(LevelType.SENIOR));
    }

    @Test
    void search_withDynamicSort() {
        // Given: 정렬 기준을 동적으로 설정 (오름차순/내림차순)
        // Given: 정렬 기준을 동적으로 설정 (등록 날짜 오름차순/내림차순)
        String sortBy = "modifiedAt";  // 등록날짜로 정렬
        boolean ascending = false;  // 유저가 현재 정렬 방향을 반대로 바꾸려고 하는 경우

        // 현재 정렬 방향의 반대로 설정
        Sort sort = Sort.by(ascending ? Sort.Order.asc(sortBy) : Sort.Order.desc(sortBy));
        PageRequest pageRequest = PageRequest.of(0, 10, sort);

        // When: 동적 정렬을 적용하여 검색
        ResumeSearchCond cond = new ResumeSearchCond();
        cond.setResumeStatus(ResumeStatus.PENDING);  // PENDING 상태로 검색

        Page<ResumeResponse> result = repository.search(cond, pageRequest);

        // Then: 반환된 결과가 동적 정렬 기준에 맞게 정렬되었는지 확인
        List<ResumeResponse> content = result.getContent();

        // 오름차순일 경우 첫 번째 값이 두 번째 값보다 작거나 같아야 함 (등록 날짜가 더 최신)
        if (ascending) {
            assertThat(content).isSortedAccordingTo(Comparator.comparing(ResumeResponse::getModifiedAt));
        } else {
            // 내림차순일 경우 첫 번째 값이 두 번째 값보다 크거나 같아야 함 (등록 날짜가 더 오래됨)
            assertThat(content).isSortedAccordingTo(Comparator.comparing(ResumeResponse::getModifiedAt).reversed());
        }
    }
}