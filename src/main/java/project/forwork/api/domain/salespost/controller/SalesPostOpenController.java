package project.forwork.api.domain.salespost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.salespost.controller.model.SalesPostPage;
import project.forwork.api.domain.salespost.controller.model.SalesPostResponse;
import project.forwork.api.domain.salespost.infrastructure.SalesPostSearchCond;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesPostSortType;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.SalesPostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/sale-resumes")
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

    @Operation(summary = "이력서 판매글 전체 조회 API",
            description = "정렬 조건 [등록날짜, 가격, 조회수, 팔린개수]" +
                    "검색 조건 [분야, 년차, 가격범위]")
    @GetMapping // 조회이기 때문에 겟매핑을 하고 다만  그러면 RequestBody를 사용못한다 TODO
    public Api<SalesPostPage> retrieveAllByCondition(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "3") int limit,
            @RequestParam SalesPostSortType sortType,
            @ModelAttribute SalesPostSearchCond cond
    ){
        SalesPostPage result = salesPostService.getResumesByCondition(offset, limit, sortType, cond);
        return Api.OK(result);
    }
}
