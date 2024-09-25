package project.forwork.api.domain.salespost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.salespost.controller.model.SalesPostPage;
import project.forwork.api.domain.salespost.controller.model.SalesPostPage3;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.infrastructure.SalesPostSearchCond;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.SalesPostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/v1/sale-resumes")
@Tag(name = "SalesPostOpenController", description = "모든 사용자를 위한 이력서 판매글 컨트롤러")
public class SalesPostOpenController {

    private final SalesPostService salesPostService;

    @Operation(summary = "단건 sale-post 조회 API", description = "sale-post 상세")
    @GetMapping("{salesPostId}")
    public Api<SalesPostResponse> retrieve(
            @PathVariable Long salesPostId
    ){
        SalesPost salesPost = salesPostService.getSellingPostWithThrow(salesPostId);
        return Api.OK(SalesPostResponse.from(salesPost));
    }

    @Operation(summary = "첫 페이지 sale-post 조회 API", description = "필터링 및 정렬 조건을 선택 할 경우 첫 페이지로 이동")
    @GetMapping("/first")
    public Api<SalesPostPage> findFirstPage(
            @RequestParam SalesPostSearchCond cond,
            @RequestParam SalesPostSortType sortType,
            @RequestParam(defaultValue = "6") int limit
    ){
        SalesPostPage salesPostPage = salesPostService.findFirstPage(sortType, cond, limit);
        return Api.OK(salesPostPage);
    }

    @Operation(summary = "마지막 페이지 sale-post 조회 API", description = "마지막 페이지로 바로 이동")
    @GetMapping("/last")
    public Api<SalesPostPage> findLastPage(
            @RequestParam SalesPostSearchCond cond,  // 새로운 필터링 조건
            @RequestParam SalesPostSortType sortType,
            @RequestParam(defaultValue = "6") int limit
    ){
        SalesPostPage salesPostPage = salesPostService.findLastPage(sortType, cond, limit);
        return Api.OK(salesPostPage);
    }

    @Operation(summary = "다음 페이지 sale-post 조회 API", description = "다음 페이지로 이동")
    @GetMapping("/next")
    public Api<SalesPostPage> findNextPage(
            @RequestParam SalesPostSearchCond cond,  // 새로운 필터링 조건
            @RequestParam SalesPostSortType sortType,
            @RequestParam(defaultValue = "6") int limit,
            @RequestParam Long lastId
    ){
        SalesPostPage salesPostPage = salesPostService.findNextPage(sortType, cond, limit, lastId);
        return Api.OK(salesPostPage);
    }

    @Operation(summary = "이전 페이지 sale-post 조회 API", description = "이전 페이지로 이동")
    @GetMapping("/previous")
    public Api<SalesPostPage> findPreviousPage(
            @RequestParam SalesPostSearchCond cond,  // 새로운 필터링 조건
            @RequestParam SalesPostSortType sortType,
            @RequestParam(defaultValue = "6") int limit,
            @RequestParam Long lastId
    ){
        SalesPostPage salesPostPage = salesPostService.findPreviousPage(sortType, cond, limit, lastId);
        return Api.OK(salesPostPage);
    }
}
