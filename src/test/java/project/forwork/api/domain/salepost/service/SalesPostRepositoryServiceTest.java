package project.forwork.api.domain.salepost.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import project.forwork.api.common.infrastructure.enums.FieldType;
import project.forwork.api.common.infrastructure.enums.LevelType;
import project.forwork.api.domain.salespost.controller.model.SalesPostFilterCond;
import project.forwork.api.domain.salespost.controller.model.SalesPostPage;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.infrastructure.SalesPostRepositoryCustomImpl;
import project.forwork.api.domain.salespost.infrastructure.SalesPostRepositoryImpl;
import project.forwork.api.domain.salespost.infrastructure.enums.FieldCond;
import project.forwork.api.domain.salespost.infrastructure.enums.LevelCond;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;
import project.forwork.api.domain.salespost.service.SalesPostService;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;
import project.forwork.api.domain.user.infrastructure.UserRepositoryImpl;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:repository-custom-test.yml")
@DataJpaTest
@Import({SalesPostRepositoryCustomImpl.class, SalesPostService.class, UserRepositoryImpl.class, SalesPostRepositoryImpl.class})
@SqlGroup({
        @Sql(value = "/sql/user-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/thumbnail-image-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/resume-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/sales-post-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Slf4j
class SalesPostRepositoryServiceTest {

    @Autowired
    private SalesPostRepositoryCustomImpl repository;

    @Autowired
    private SalesPostService salesPostService;

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
        List<SalesPostResponse> result = salesPostService.findFirstPage(cond, 2).getResults();

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
        List<SalesPostResponse> result = salesPostService.findFirstPage(cond, 2).getResults();

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(7L);
        assertThat(result.get(1).getId()).isEqualTo(5L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.BACKEND));
    }

    @Test
    void 최소_가격_첫_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, new BigDecimal("60000"), null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = salesPostService.findFirstPage(cond, 2).getResults();
        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPrice()).isGreaterThan(new BigDecimal("60000"));
        assertThat(result.get(1).getPrice()).isGreaterThan(new BigDecimal("60000"));
    }

    @Test
    void 최대_가격_첫_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, null, new BigDecimal("50000"), null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = salesPostService.findFirstPage(cond, 2).getResults();
        //then(검증)
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(7L);
        assertThat(result.get(0).getPrice()).isLessThan(new BigDecimal("50000"));
    }

    @Test
    void 가격_범위_첫_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, new BigDecimal("80000"), new BigDecimal("100000"), null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findFirstPage(cond, 10);
        //then(검증)
    }

/*    @Test
    void 조건_BACKEND_조회순_첫_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from
                (SalesPostSortType., null, null, FieldCond.BACKEND, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findFirstPage(cond, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(5L);
        assertThat(result.get(1).getId()).isEqualTo(7L);
        assertThat(result.get(2).getId()).isEqualTo(1L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.BACKEND));
    }*/

/*    @Test
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
    }*/

    @Test
    void 조건_없이_다음_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        SalesPostPage salesPostPage = salesPostService.findNextPage(cond, 7L, 2);
        List<SalesPostResponse> result = salesPostPage.getResults();

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(6L);
        assertThat(result.get(1).getId()).isEqualTo(5L);
        assertThat(salesPostPage.getLastId()).isEqualTo(5L);
    }

    @Test
    void 정렬조건_OLD_다음_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.OLD, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        SalesPostPage salesPostPage = salesPostService.findNextPage(cond, 5L, 2);
        List<SalesPostResponse> result = salesPostPage.getResults();

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(6L);
        assertThat(result.get(1).getId()).isEqualTo(7L);
        assertThat(salesPostPage.getLastId()).isEqualTo(7L);
    }

    @Test
    void 정렬조건_NEW_다음_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.NEW, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findNextPage(cond, 5L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(4L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    // 8 2 1 5 4 6 7
    void 정렬조건_HIGHEST_PRICE_다음_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.HIGHEST_PRICE, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findNextPage(cond, 8L, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(1).getId()).isEqualTo(1L);
        assertThat(result.get(2).getId()).isEqualTo(5L);
    }

    @Test
    void 정렬조건_LOWEST_PRICE_다음_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.LOWEST_PRICE, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findNextPage(cond, 7L, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(6L);
        assertThat(result.get(1).getId()).isEqualTo(4L);
        assertThat(result.get(2).getId()).isEqualTo(5L);
    }

    //6 7 2 4 8 1 5
    @Test
    void 정렬조건_BEST_SELLING_다음_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.BEST_SELLING, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findNextPage(cond, 2L, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(4L);
        assertThat(result.get(1).getId()).isEqualTo(8L);
        assertThat(result.get(2).getId()).isEqualTo(1L);
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
        List<SalesPostResponse> result = repository.findNextPage(cond, 1L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(4L);
        assertThat(result.get(1).getId()).isEqualTo(5L);
        assertThat(result).allMatch(salesPostResponse -> salesPostResponse.getField().equals(FieldType.BACKEND));
    }

    @Test
    void 조건_없이_이전_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(null, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findPreviousPage(cond, 4L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(6L);
        assertThat(result.get(1).getId()).isEqualTo(5L);
    }

    @Test
    void 정렬조건_NEW_이전_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.NEW, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        SalesPostPage salesPostPage = salesPostService.findPreviousPage(cond, 4L, 2);
        List<SalesPostResponse> result = salesPostPage.getResults();

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(6L);
        assertThat(result.get(1).getId()).isEqualTo(5L);
        assertThat(salesPostPage.getLastId()).isEqualTo(6L);

        SalesPostPage salesPostPage2 = salesPostService.findPreviousPage(cond, 6L, 2);
        List<SalesPostResponse> result2 = salesPostPage2.getResults();

        assertThat(result2).hasSize(2);
        assertThat(result2.get(0).getId()).isEqualTo(8L);
        assertThat(result2.get(1).getId()).isEqualTo(7L);
        assertThat(salesPostPage2.getLastId()).isEqualTo(8L);
    }

    @Test
    void 정렬조건_OLD_이전_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.OLD, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findPreviousPage(cond, 5L, 2);

        //then(검증)
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(1).getId()).isEqualTo(4L);
    }

    @Test
    // 8 2 1 5 4 6 7
    void 정렬조건_HIGHEST_PRICE_이전_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.HIGHEST_PRICE, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findPreviousPage(cond, 4L, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(2L); // 2
        assertThat(result.get(1).getId()).isEqualTo(1L); // 1
        assertThat(result.get(2).getId()).isEqualTo(5L); // 5
    }

    @Test
    void 정렬조건_LOEST_PRICE_이전_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.LOWEST_PRICE, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findPreviousPage(cond, 5L, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(7L);
        assertThat(result.get(1).getId()).isEqualTo(6L);
        assertThat(result.get(2).getId()).isEqualTo(4L);
    }

    //6 7 2 4 8 1 5
    @Test
    void 정렬조건_BEST_SELLING_이전_페이지_검색(){
        //given(상황환경 세팅)
        SalesPostFilterCond cond = SalesPostFilterCond.from(SalesPostSortType.BEST_SELLING, null, null, null, null);

        //when(상황발생) 기본 정렬 최신 등록순
        List<SalesPostResponse> result = repository.findPreviousPage(cond, 5L, 3);

        //then(검증)
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(4L);
        assertThat(result.get(1).getId()).isEqualTo(8L);
        assertThat(result.get(2).getId()).isEqualTo(1L);
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

/*    @Test
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
    }*/
}