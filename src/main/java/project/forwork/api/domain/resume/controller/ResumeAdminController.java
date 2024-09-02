package project.forwork.api.domain.resume.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.resume.controller.model.ResumePage;
import project.forwork.api.domain.resume.infrastructure.querydsl.ResumeSearchCond;
import project.forwork.api.domain.resume.service.ResumeService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "ResumeController for Admin", description = "어드민 전용 판매 이력서 조회 컨트롤러")

public class ResumeAdminController {

    private final ResumeService resumeService;

    @Operation(summary = "이력서 판매 요청 전체 조회 API",
            description = "요청시간, 결정시간 오름차순, 내림차순으로 정렬 선택 가능" +
                          "검색 조건 [분야, 년차, 판매여부 상태]")
    @GetMapping("/resumes")
    public Api<ResumePage> getResumesByCondition(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "registeredAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @ModelAttribute ResumeSearchCond cond
    ){
        ResumePage result = resumeService.getResumesByCondition(offset, limit, sortBy, ascending, cond);
        return Api.OK(result);
    }
}
