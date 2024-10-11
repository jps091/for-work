package project.forwork.api.domain.resumedecision.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.resumedecision.service.ResumeDecisionService;
import project.forwork.api.common.domain.CurrentUser;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin-api/v1")
@Tag(name = "ResumeDecisionController for Admin", description = "어드민 전용 판매 이력서 관리 컨트롤러")

public class ResumeDecisionController {

    private final ResumeDecisionService resumeDecisionService;

    @Operation(summary = "이력서 판매 요청 승인 API", description = "이력서 판매 요청 승인 API")
    @PostMapping("/resume/{resumeId}/approve")
    public Api<String> approve(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long resumeId
    ){
        resumeDecisionService.approve(currentUser, resumeId);
        return Api.OK("승인 완료 했습니다.");
    }

    @Operation(summary = "이력서 판매 요청 거부 API", description = "이력서 판매 요청 거부 API")
    @PostMapping("/resume/{resumeId}/deny")
    public Api<String> deny(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long resumeId
    ){
        resumeDecisionService.deny(currentUser, resumeId);
        return Api.OK("거부 되었습니다.");
    }
}
