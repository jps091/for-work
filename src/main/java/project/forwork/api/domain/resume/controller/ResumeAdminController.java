package project.forwork.api.domain.resume.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.resume.controller.model.ResumeDetailResponse;
import project.forwork.api.domain.resume.controller.model.ResumePage;
import project.forwork.api.domain.resume.infrastructure.ResumeSearchCond;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.ResumeService;
import project.forwork.api.common.domain.CurrentUser;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin-api/resumes")
@Tag(name = "ResumeController for Admin", description = "어드민 전용 판매 이력서 조회 컨트롤러")

public class ResumeAdminController {

    private final ResumeService resumeService;

    @Operation(summary = "이력서 판매 요청 전체 조회 API",
            description = "요청시간, 결정시간 오름차순, 내림차순으로 정렬 선택 가능" +
                          "검색 조건 [분야, 년차, 판매여부 상태]")
    @GetMapping
    public Api<ResumePage> retrieveAllByCondition(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "modifiedAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @ModelAttribute ResumeSearchCond cond
    ){
        ResumePage result = resumeService.getResumesByCondition(offset, limit, sortBy, ascending, cond);
        return Api.OK(result);
    }

    @Operation(summary = "요청 Resume 상세 조회 API", description = "요청 Resume 상세 조회 API")
    @GetMapping("{resumeId}")
    public Api<ResumeDetailResponse> retrieve(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long resumeId
    ){
        Resume resume = resumeService.getByIdWithThrow(currentUser, resumeId);
        return Api.OK(ResumeDetailResponse.from(resume));
    }
}
