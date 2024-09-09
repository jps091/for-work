package project.forwork.api.domain.salepost.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.salepost.controller.model.SalePostResponse;
import project.forwork.api.domain.salepost.model.SalePost;
import project.forwork.api.domain.salepost.service.SalePostService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/sale-resumes")
@Tag(name = "SalePostController", description = "이력서 판매글 관리 컨트롤러")
public class SalePostController {

    private final SalePostService salePostService;

    @Operation(summary = "sale-resume 생성", description = "sale-resume 생성")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{resumeId}/register")
    public Api<SalePostResponse> register(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long resumeId
    ){
        SalePost salePost = salePostService.register(currentUser, resumeId);
        return Api.CREATED(SalePostResponse.from(salePost));
    }

    @Operation(summary = "sale-resume 판매 재게", description = "자신의 resume이 상태가 active일때만 가능")
    @PutMapping("/{resumeId}/start")
    public Api<String> startSelling(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long resumeId
    ){
        salePostService.startSelling(currentUser, resumeId);
        return Api.OK("판매중으로 변경 완료 하였습니다.");
    }

    @Operation(summary = "sale-resume 판매 중단", description = "자신의 resume이 상태가 active일때만 가능")
    @PutMapping("/{resumeId}/cancel")
    public Api<String> cancelSelling(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long resumeId
    ){
        salePostService.cancelSelling(currentUser, resumeId);
        return Api.OK("판매 중단으로 변경 완료 하였습니다.");
    }
}
