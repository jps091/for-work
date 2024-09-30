package project.forwork.api.domain.salepost.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.salespost.controller.model.SalesPostFilterCond;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.infrastructure.SalesPostRepositoryCustomImpl;
import project.forwork.api.domain.salespost.infrastructure.enums.FieldCond;
import project.forwork.api.domain.salespost.infrastructure.enums.LevelCond;
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
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findFirstPage(cond, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(8L);
        assertThat(result.get(1).getId()).isEqualTo(7L);
    }
// 7,5,4,3,1 back
    @Test
    void 조건_BACKEND_첫_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, null, null, FieldCond.BACKEND, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findFirstPage(cond, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(7L);
        assertThat(result.get(1).getId()).isEqualTo(5L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.BACKEND));
    }

    @Test
    void 조건_BACKEND_조회순_첫_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from
                (SalesPostSortType.VIEW_COUNT, null, null, FieldCond.BACKEND, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findFirstPage(cond, 3);

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
        SalesPostFilterCond cond = SalesPostFilterCond.from
                (SalesPostSortType.VIEW_COUNT, new BigDecimal("60000.00"), new BigDecimal("90000.00"), FieldCond.BACKEND, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findFirstPage(cond, 3);

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
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findNextPage(cond, 7L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(6L);
        assertThat(result.get(1).getId()).isEqualTo(5L);
    }

    @Test
    void 조건_AI_다음_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, null, null, FieldCond.AI, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findNextPage(cond, 7L, 1);

        //then(검증)
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.AI));
    }

    @Test
    void 조건_BACKEND_오랜된순_다음_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.OLD, null, null, FieldCond.BACKEND, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findNextPage(cond, 5L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(4L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.BACKEND));
    }

    @Test
    void 조건_없이_이전_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findPreviousPage(cond, 1L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(4L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void 조건_SENIOR_이전_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, null, null, null, LevelCond.SENIOR);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findPreviousPage(cond, 1L, 1);

        //then(검증)
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getLevel().equals(LevelType.SENIOR));
    }

    @Test
    void 조건_없이_마지막_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findLastPage(cond, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(1).getId()).isEqualTo(1L);
    }

    @Test
    void 조건_BACKEND_조회순_마지막_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.VIEW_COUNT, null, null, FieldCond.BACKEND, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findLastPage(cond, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(7L);
        assertThat(result.get(1).getId()).isEqualTo(1L);
        assertThat(result.get(2).getId()).isEqualTo(4L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.BACKEND));
    }
}