package project.forwork.api.domain.salepost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.salepost.controller.model.SalePostPage;
import project.forwork.api.domain.salepost.controller.model.SalePostResponse;
import project.forwork.api.domain.salepost.infrastructure.SalePostSearchCond;
import project.forwork.api.domain.salepost.infrastructure.enums.SalePostSortType;
import project.forwork.api.domain.salepost.model.SalePost;
import project.forwork.api.domain.salepost.service.SalePostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/sale-resumes")
@Tag(name = "SalePostOpenController", description = "모든 사용자를 위한 이력서 판매글 컨트롤러")
public class SalePostOpenController {

    private final SalePostService salePostService;

    @Operation(summary = "단건 sale-post 조회 API", description = "sale-post 상세")
    @GetMapping("{salePostId}")
    public Api<SalePostResponse> retrieve(
            @PathVariable Long salePostId
    ){
        SalePost salePost = salePostService.getSellingPostWithThrow(salePostId);
        return Api.OK(SalePostResponse.from(salePost));
    }

    @Operation(summary = "이력서 판매글 전체 조회 API",
            description = "정렬 조건 [등록날짜, 가격, 조회수, 팔린개수]" +
                    "검색 조건 [분야, 년차, 가격범위, 제목]")
    @GetMapping // 조회이기 때문에 겟매핑을 하고 다만  그러면 RequestBody를 사용못한다 TODO
    public Api<SalePostPage> retrieveAllByCondition(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam SalePostSortType sortType,
            @ModelAttribute SalePostSearchCond cond
    ){
        SalePostPage result = salePostService.getResumesByCondition(offset, limit, sortType, cond);
        return Api.OK(result);
    }
}
