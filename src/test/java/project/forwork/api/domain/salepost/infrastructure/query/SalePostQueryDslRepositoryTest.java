package project.forwork.api.domain.salepost.infrastructure.query;

import jakarta.persistence.FetchType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.salepost.controller.model.SalePostResponse;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("classpath:test-application.yml")
@SqlGroup({
        @Sql(value = "/sql/resume-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/sale-post-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class SalePostQueryDslRepositoryTest {

    @Autowired
    private SalePostQueryDslRepository repository;

    /***
     * ('test1', 'FRONTEND', 'NEW', 'PENDING')
     * ('test2', 'AI', 'SENIOR', 'REJECTED')
     * ('test3', 'BACKEND', 'JUNIOR', 'PENDING')
     * ('test4', 'FRONTEND', 'NEW', 'ACTIVE') // cancel
     */

    @Test
    void 조건_없이_검색(){
        //given(상황환경 세팅)
        SalePostSearchCond cond = new SalePostSearchCond();
        PageRequest pageRequest = PageRequest.of(0, 10);


        //when(상황발생)
        Page<SalePostResponse> result = repository.search(cond, pageRequest, SalePostSortType.BEST_SELLING);
        List<SalePostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).hasSize(7);
    }

    @Test
    void 조건_분야_검색(){
        //given(상황환경 세팅)
        SalePostSearchCond cond = new SalePostSearchCond();
        cond.setField(FieldType.FRONTEND);
        PageRequest pageRequest = PageRequest.of(0, 10);


        //when(상황발생)
        Page<SalePostResponse> result = repository.search(cond, pageRequest, SalePostSortType.BEST_SELLING);
        List<SalePostResponse> content = result.getContent();

        //then(검증)
        assertThat(content.get(0).getField()).isEqualTo(FieldType.FRONTEND);
    }

    @Test
    void 이력서_레벨조건_검색() {
        //given(상황환경 세팅)
        SalePostSearchCond cond = new SalePostSearchCond();
        cond.setLevel(LevelType.NEW);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalePostResponse> result = repository.search(cond, pageRequest, SalePostSortType.BEST_SELLING);
        List<SalePostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).allMatch(resume -> resume.getLevel().equals(LevelType.NEW));
    }

    @Test
    void 이력서_가격범위_검색1() {
        //given(상황환경 세팅)
        SalePostSearchCond cond = new SalePostSearchCond();
        cond.setMinPrice(new BigDecimal(75000));

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalePostResponse> result = repository.search(cond, pageRequest, SalePostSortType.BEST_SELLING);
        List<SalePostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(s -> s.getPrice().compareTo(new BigDecimal(75000)) >= 0);
    }

    @Test
    void 이력서_가격범위_검색2() {
        //given(상황환경 세팅)
        SalePostSearchCond cond = new SalePostSearchCond();
        cond.setMaxPrice(new BigDecimal(90000));

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalePostResponse> result = repository.search(cond, pageRequest, SalePostSortType.BEST_SELLING);
        List<SalePostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(s -> s.getPrice().compareTo(new BigDecimal(90000)) <= 0);
    }

    @Test
    void 이력서_가격범위_검색3() {
        //given(상황환경 세팅)
        SalePostSearchCond cond = new SalePostSearchCond();
        cond.setMinPrice(new BigDecimal(50000));
        cond.setMaxPrice(new BigDecimal(100000));

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalePostResponse> result = repository.search(cond, pageRequest, SalePostSortType.BEST_SELLING);
        List<SalePostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(s -> s.getPrice().compareTo(new BigDecimal(50000)) >= 0 &&
                s.getPrice().compareTo(new BigDecimal(100000)) <= 0);
    }

    @Test
    void 이력서_제목_검색1() {
        //given(상황환경 세팅)
        SalePostSearchCond cond = new SalePostSearchCond();
        cond.setTitle("test");

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalePostResponse> result = repository.search(cond, pageRequest, SalePostSortType.BEST_SELLING);
        List<SalePostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).allMatch(s -> s.getTitle().contains("test"));
    }

    @Test
    void 이력서_제목_검색2() {
        //given(상황환경 세팅)
        SalePostSearchCond cond = new SalePostSearchCond();
        cond.setTitle("백엔드");

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalePostResponse> result = repository.search(cond, pageRequest, SalePostSortType.BEST_SELLING);
        List<SalePostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).allMatch(s -> s.getTitle().contains("백엔드"));
    }

    @Test
    void 이력서_가격범위_분야_검색() {
        //given(상황환경 세팅)
        SalePostSearchCond cond = new SalePostSearchCond();
        cond.setMinPrice(new BigDecimal(50000));
        cond.setMaxPrice(new BigDecimal(100000));
        cond.setLevel(LevelType.NEW);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalePostResponse> result = repository.search(cond, pageRequest, SalePostSortType.BEST_SELLING);
        List<SalePostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(s -> s.getLevel().equals(LevelType.NEW));
        assertThat(content).allMatch(s -> s.getPrice().compareTo(new BigDecimal(50000)) >= 0 &&
                s.getPrice().compareTo(new BigDecimal(100000)) <= 0);
    }

    @Test
    void 이력서_가격범위_년차_검색() {
        //given(상황환경 세팅)
        SalePostSearchCond cond = new SalePostSearchCond();
        cond.setMinPrice(new BigDecimal(50000));
        cond.setMaxPrice(new BigDecimal(100000));
        cond.setLevel(LevelType.NEW);
        cond.setField(FieldType.FRONTEND);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalePostResponse> result = repository.search(cond, pageRequest, SalePostSortType.BEST_SELLING);
        List<SalePostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(s -> s.getLevel().equals(LevelType.NEW));
        assertThat(content).allMatch(s -> s.getField().equals(FieldType.FRONTEND));
        assertThat(content).allMatch(s -> s.getPrice().compareTo(new BigDecimal(50000)) >= 0 &&
                s.getPrice().compareTo(new BigDecimal(100000)) <= 0);
    }
}