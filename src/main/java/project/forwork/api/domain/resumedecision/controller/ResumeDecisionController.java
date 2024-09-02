package project.forwork.api.domain.resumedecision.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.resume.controller.model.ResumePage;
import project.forwork.api.domain.resumedecision.controller.model.ResumeDecisionResponse;
import project.forwork.api.domain.resume.infrastructure.querydsl.ResumeSearchCond;
import project.forwork.api.domain.resumedecision.service.ResumeDecisionService;
import project.forwork.api.domain.user.controller.model.CurrentUser;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "ResumeDecisionController for Admin", description = "어드민 전용 판매 이력서 관리 컨트롤러")

public class ResumeDecisionController {

    private final ResumeDecisionService resumeDecisionService;

    @Operation(summary = "이력서 판매 요청 승인 API", description = "이력서 판매 요청 승인 API")
    @PostMapping("/resume/{resumeDecisionId}/approve")
    public Api<ResumeDecisionResponse> approveResumeSale(
            @Current CurrentUser currentUser,
            @PathVariable Long resumeDecisionId
    ){
        ResumeDecisionResponse approveResume = resumeDecisionService.approve(currentUser, resumeDecisionId);
        return Api.OK(approveResume);
    }

    @Operation(summary = "이력서 판매 요청 거부 API", description = "이력서 판매 요청 거부 API")
    @PostMapping("/resume/{resumeDecisionId}/deny")
    public Api<ResumeDecisionResponse> denyResumeDeny(
            @Current CurrentUser currentUser,
            @PathVariable Long resumeDecisionId
    ){
        ResumeDecisionResponse denyResume = resumeDecisionService.deny(currentUser, resumeDecisionId);
        return Api.OK(denyResume);
    }

/*    @Operation(summary = "관리자 권한 Post 삭제 API", description = "삭제할 post_id 입력")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Api<String>> deletePostByAdmin(
            HttpServletRequest request,
            @Parameter(description = "관리자 권한으로 삭제 할 Post ID", required = true, example = "1")
            @PathVariable Long postId
    ){
        userRoleBusiness.deletePostByAdmin(request, postId);
        return ResponseEntity.status(HttpStatus.OK).body(Api.OK("글 삭제가 완료 되었습니다."));
    }*/
}
