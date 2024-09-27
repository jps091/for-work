package project.forwork.api.domain.salepost.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.infrastructure.SalesPostRepositoryCustomImpl;
import project.forwork.api.domain.salespost.infrastructure.SalesPostSearchCond;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@TestPropertySource("classpath:test-application.yml")
@DataJpaTest
@Import({SalesPostRepositoryCustomImpl.class})
@SqlGroup({
        @Sql(value = "/sql/user-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/resume-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/sales-post-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class SalesPostRepositoryCustomImplTest {

    @Autowired
    private SalesPostRepositoryCustomImpl repository;

    /***
     * ('test1', 'FRONTEND', 'NEW', 'PENDING')
     * ('test2', 'AI', 'SENIOR', 'REJECTED')
     * ('test3', 'BACKEND', 'JUNIOR', 'PENDING')
     * ('test4', 'FRONTEND', 'NEW', 'ACTIVE') // cancel
     */

    @Test
    void 조건_없이_첫_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository
                .findFirstPage(null, null, null, null, null, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(8L);
        assertThat(result.get(1).getId()).isEqualTo(7L);
    }
// 7,5,4,3,1 back
    @Test
    void 조건_BACKEND_첫_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();
        cond.setField(FieldType.BACKEND);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository
                .findFirstPage(null, null, null, FieldType.BACKEND, null, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(7L);
        assertThat(result.get(1).getId()).isEqualTo(5L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.BACKEND));
    }

    @Test
    void 조건_BACKEND_조회순_첫_페이지_검색(){
        //given(상황환경 세팅)
        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository
                .findFirstPage(SalesPostSortType.VIEW_COUNT, null, null, FieldType.BACKEND, null, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(5L);
        assertThat(result.get(1).getId()).isEqualTo(7L);
        assertThat(result.get(2).getId()).isEqualTo(1L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.BACKEND));
    }

    @Test
    void 조건_BACKEND_가격_범위_조회순_첫_페이지_검색(){
        //given(상황환경 세팅)
        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository
                .findFirstPage(SalesPostSortType.VIEW_COUNT, new BigDecimal("60000.00"), new BigDecimal("90000.00"), FieldType.BACKEND, null, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(5L);
        assertThat(result.get(1).getId()).isEqualTo(1L);
        assertThat(result.get(2).getId()).isEqualTo(4L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.BACKEND));
    }

    @Test
    void 조건_없이_다음_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository
                .findNextPage(null, null, null, null, null, 7L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(6L);
        assertThat(result.get(1).getId()).isEqualTo(5L);
    }

    @Test
    void 조건_AI_다음_페이지_검색(){
        //given(상황환경 세팅)
        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository
                .findNextPage(null, null, null, FieldType.AI, null, 7L, 1);

        //then(검증)
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.AI));
    }

    @Test
    void 조건_BACKEND_오랜된순_다음_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();
        cond.setField(FieldType.BACKEND);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository
                .findNextPage(SalesPostSortType.OLD, null, null, FieldType.BACKEND, null, 5L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(4L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.BACKEND));
    }

    @Test
    void 조건_없이_이전_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository
                .findPreviousPage(null, null, null, null, null, 1L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(4L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void 조건_SENIOR_이전_페이지_검색(){
        //given(상황환경 세팅)
        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository
                .findPreviousPage(null, null, null, null, LevelType.SENIOR, 1L, 1);

        //then(검증)
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getLevel().equals(LevelType.SENIOR));
    }

    @Test
    void 조건_없이_마지막_페이지_검색(){
        //given(상황환경 세팅)
        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository
                .findLastPage(null, null, null, null, null, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(1).getId()).isEqualTo(1L);
    }

    @Test
    void 조건_BACKEND_조회순_마지막_페이지_검색(){
        //given(상황환경 세팅)
        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository
                .findLastPage(SalesPostSortType.VIEW_COUNT, null, null, FieldType.BACKEND, null, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(7L);
        assertThat(result.get(1).getId()).isEqualTo(1L);
        assertThat(result.get(2).getId()).isEqualTo(4L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.BACKEND));
    }

    @Test
    void 조건_없이_검색(){
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();
        PageRequest pageRequest = PageRequest.of(0, 10);


        //when(상황발생)
        Page<SalesPostResponse> result = repository.searchByCondition(cond, pageRequest, SalesPostSortType.BEST_SELLING);
        List<SalesPostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).hasSize(7);
    }

    @Test
    void 조건_분야_검색(){
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();
        cond.setField(FieldType.FRONTEND);
        PageRequest pageRequest = PageRequest.of(0, 10);


        //when(상황발생)
        Page<SalesPostResponse> result = repository.searchByCondition(cond, pageRequest, SalesPostSortType.BEST_SELLING);
        List<SalesPostResponse> content = result.getContent();

        //then(검증)
        assertThat(content.get(0).getField()).isEqualTo(FieldType.FRONTEND);
    }

    @Test
    void 이력서_레벨조건_검색() {
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();
        cond.setLevel(LevelType.NEW);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalesPostResponse> result = repository.searchByCondition(cond, pageRequest, SalesPostSortType.BEST_SELLING);
        List<SalesPostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).allMatch(resume -> resume.getLevel().equals(LevelType.NEW));
    }

    @Test
    void 이력서_가격범위_검색1() {
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();
        cond.setMinPrice(new BigDecimal(75000));

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalesPostResponse> result = repository.searchByCondition(cond, pageRequest, SalesPostSortType.BEST_SELLING);
        List<SalesPostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(s -> s.getPrice().compareTo(new BigDecimal(75000)) >= 0);
    }

    @Test
    void 이력서_가격범위_검색2() {
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();
        cond.setMaxPrice(new BigDecimal(90000));

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalesPostResponse> result = repository.searchByCondition(cond, pageRequest, SalesPostSortType.BEST_SELLING);
        List<SalesPostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(s -> s.getPrice().compareTo(new BigDecimal(90000)) <= 0);
    }

    @Test
    void 이력서_가격범위_검색3() {
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();
        cond.setMinPrice(new BigDecimal(50000));
        cond.setMaxPrice(new BigDecimal(100000));

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalesPostResponse> result = repository.searchByCondition(cond, pageRequest, SalesPostSortType.BEST_SELLING);
        List<SalesPostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(s -> s.getPrice().compareTo(new BigDecimal(50000)) >= 0 &&
                s.getPrice().compareTo(new BigDecimal(100000)) <= 0);
    }

    @Test
    void 이력서_가격범위_분야_검색() {
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();
        cond.setMinPrice(new BigDecimal(50000));
        cond.setMaxPrice(new BigDecimal(100000));
        cond.setLevel(LevelType.NEW);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalesPostResponse> result = repository.searchByCondition(cond, pageRequest, SalesPostSortType.BEST_SELLING);
        List<SalesPostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(s -> s.getLevel().equals(LevelType.NEW));
        assertThat(content).allMatch(s -> s.getPrice().compareTo(new BigDecimal(50000)) >= 0 &&
                s.getPrice().compareTo(new BigDecimal(100000)) <= 0);
    }

    @Test
    void 이력서_가격범위_분야_년차_검색() {
        //given(상황환경 세팅)
        SalesPostSearchCond cond = new SalesPostSearchCond();
        cond.setMinPrice(new BigDecimal(10000));
        cond.setMaxPrice(new BigDecimal(99000));
        cond.setLevel(LevelType.NEW);
        cond.setField(FieldType.BACKEND);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when(상황발생)
        Page<SalesPostResponse> result = repository.searchByCondition(cond, pageRequest, SalesPostSortType.BEST_SELLING);
        List<SalesPostResponse> content = result.getContent();

        //then(검증)
        assertThat(content).isNotEmpty();
        assertThat(content).allMatch(s -> s.getLevel().equals(LevelType.NEW));
        assertThat(content).allMatch(s -> s.getField().equals(FieldType.BACKEND));
        assertThat(content).allMatch(s -> s.getPrice().compareTo(new BigDecimal(10000)) >= 0 &&
                s.getPrice().compareTo(new BigDecimal(99000)) <= 0);
    }
}