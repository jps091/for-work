package project.forwork.api.domain.salespost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.resume.infrastructure.enums.FieldType;
import project.forwork.api.domain.resume.infrastructure.enums.LevelType;
import project.forwork.api.domain.salespost.controller.model.SalesPostDetailResponse;
import project.forwork.api.domain.salespost.controller.model.SalesPostPage;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
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
    @GetMapping("{salesPostId}")
    public Api<SalesPostDetailResponse> retrieve(
            @PathVariable Long salesPostId
    ){
        SalesPostDetailResponse salesPostResponse = salesPostService.getSellingPostWithThrow(salesPostId);
        return Api.OK(salesPostResponse);
    }

    @Operation(summary = "첫 페이지 sale-post 조회 API", description = "필터링 및 정렬 조건을 선택 할 경우 첫 페이지로 이동")
    @GetMapping("/first")
    public Api<SalesPostPage> findFirstPage(
            @RequestParam(required = false) SalesPostSortType sortType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) FieldType field,
            @RequestParam(required = false) LevelType level,
            @RequestParam(defaultValue = "6") int limit
    ){
        SalesPostPage salesPostPage = salesPostService
                .findFirstPage(sortType, minPrice, maxPrice, field, level, limit);
        return Api.OK(salesPostPage);
    }

    @Operation(summary = "마지막 페이지 sale-post 조회 API", description = "마지막 페이지로 바로 이동")
    @GetMapping("/last")
    public Api<SalesPostPage> findLastPage(
            @RequestParam(required = false) SalesPostSortType sortType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) FieldType field,
            @RequestParam(required = false) LevelType level,
            @RequestParam(defaultValue = "6") int limit
    ){
        SalesPostPage salesPostPage = salesPostService
                .findLastPage(sortType, minPrice, maxPrice, field, level, limit);
        return Api.OK(salesPostPage);
    }

    @Operation(summary = "다음 페이지 sale-post 조회 API", description = "다음 페이지로 이동")
    @GetMapping("/next")
    public Api<SalesPostPage> findNextPage(
            @RequestParam(required = false) SalesPostSortType sortType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) FieldType field,
            @RequestParam(required = false) LevelType level,
            @RequestParam(defaultValue = "6") int limit,
            @RequestParam Long lastId
    ){
        SalesPostPage salesPostPage = salesPostService
                .findNextPage(sortType, minPrice, maxPrice, field, level, lastId, limit);
        return Api.OK(salesPostPage);
    }

    @Operation(summary = "이전 페이지 sale-post 조회 API", description = "이전 페이지로 이동")
    @GetMapping("/previous")
    public Api<SalesPostPage> findPreviousPage(
            @RequestParam(required = false) SalesPostSortType sortType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) FieldType field,
            @RequestParam(required = false) LevelType level,
            @RequestParam(defaultValue = "6") int limit,
            @RequestParam Long lastId
    ){
        SalesPostPage salesPostPage = salesPostService
                .findPreviousPage(sortType, minPrice, maxPrice, field, level, lastId, limit);
        return Api.OK(salesPostPage);
    }
}
