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
import project.forwork.api.domain.resume.controller.model.ResumeDetailResponse;
import project.forwork.api.domain.resume.controller.model.ResumeResponse;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salepost.controller.model.SalePostResponse;
import project.forwork.api.domain.salepost.model.SalePost;
import project.forwork.api.domain.salepost.service.SalePostService;

import java.util.List;

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

/*    @Operation(summary = "회원 Resume 전체조회 API", description = "로그인한 회원의 전체 Resume 조회")
    @GetMapping
    public Api<List<ResumeResponse>> retrieveAll(
            @Parameter(hidden = true) @Current CurrentUser currentUser
    ){
        List<ResumeResponse> resumes = resumeService.findResumesBySeller(currentUser);
        return Api.OK(resumes);
    }*/
}
