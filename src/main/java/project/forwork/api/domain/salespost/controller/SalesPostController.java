package project.forwork.api.domain.salespost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.salespost.controller.model.SalesPostSellerResponse;
import project.forwork.api.domain.salespost.infrastructure.enums.SalesStatus;
import project.forwork.api.domain.salespost.service.SalesPostService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/sales-resumes")
@Tag(name = "SalesPostController", description = "이력서 판매글 관리 컨트롤러")
public class SalesPostController {

    private final SalesPostService salesPostService;

    @Operation(summary = "자신의 판매글 조회 ", description = "판매량, 판매수량도 같이 볼수 있다.")
    @GetMapping
    public Api<List<SalesPostSellerResponse>> find(
            @Parameter(hidden = true) @Current CurrentUser currentUser
    ){
        List<SalesPostSellerResponse> salesPostResponses = salesPostService.findBySeller(currentUser);
        return Api.OK(salesPostResponses);
    }

    @Operation(summary = "sale-resume 상태 변경",
            description = "자신의 resume이 상태가 active일때만 가능\n" +
                    " SELLING, CANCEL 변경 가능")
    @PutMapping("/{salesPostId}/status")
    public Api<String> changeSalesStatus(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long salesPostId,
            @RequestParam SalesStatus status
    ){
        salesPostService.changeSalesStatus(currentUser, salesPostId, status);
        return Api.OK("판매글 상태를 변경 완료 하였습니다.");
    }
}
