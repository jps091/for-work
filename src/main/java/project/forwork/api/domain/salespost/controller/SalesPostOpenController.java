package project.forwork.api.domain.salespost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.salespost.controller.model.SalesPostDetailResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostPage;
import project.forwork.api.domain.salespost.infrastructure.enums.FieldCond;
import project.forwork.api.domain.salespost.infrastructure.enums.LevelCond;
import project.forwork.api.domain.resume.infrastructure.enums.PageStep;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;
import project.forwork.api.domain.salespost.service.SalesPostService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/v1/sales-resumes")
@Tag(name = "SalesPostOpenController", description = "모든 사용자를 위한 이력서 판매글 컨트롤러")
public class SalesPostOpenController {

    private final SalesPostService salesPostService;

    @Operation(summary = "단건 sale-post 조회 API", description = "sale-post 상세")
    @GetMapping("/{salesPostId}")
    public Api<SalesPostDetailResponse> retrieve(
            @PathVariable Long salesPostId
    ){
        SalesPostDetailResponse salesPostResponse = salesPostService.getSellingPost(salesPostId);
        return Api.OK(salesPostResponse);
    }

    @Operation(summary = "전체 sale-post 조회 API",
            description = """
                    sortType 정렬조건[OLD, NEW, HIGHEST_PRICE, LOWEST_PRICE, VIEW_COUNT, BEST_SELLING, DEFAULT]
                     minPrice, maxPrice 가격 범위 필터링 조건
                     분야 조건 : field [FRONTEND BACKEND ANDROID IOS DEVOPS AI ETC UNSELECTED]
                     년차 조건 : level [NEW, JUNIOR, SENIOR]
                     pageStep : [FIRST(default), NEXT, PREVIOUS, LAST]
                     limit 가져올 개수 기본 6개
                     lastId : 이전, 다음 페이지 호출시 반드시 필요
                    """)
    @GetMapping("/page")
    public Api<SalesPostPage> getFilteredPage(
            @RequestParam(required = false) SalesPostSortType sortType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) FieldCond field,
            @RequestParam(required = false) LevelCond level,
            @RequestParam(defaultValue = "FIRST")PageStep pageStep,
            @RequestParam(defaultValue = "6") int limit,
            @RequestParam(required = false) Long lastId
    ) {
        // 필터링 및 페이징을 처리하는 서비스 호출
        SalesPostPage result = salesPostService
                .getFilteredAndPagedResults(sortType, minPrice, maxPrice, field, level, pageStep, lastId, limit);
        return Api.OK(result);
    }
}
