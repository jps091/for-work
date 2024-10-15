package project.forwork.api.domain.resume.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.resume.controller.model.ResumeAdminDetailResponse;
import project.forwork.api.domain.resume.controller.model.ResumePage;
import project.forwork.api.domain.resume.infrastructure.enums.PageStep;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.ResumeService;
import project.forwork.api.common.domain.CurrentUser;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin-api/v1/resumes")
@Tag(name = "ResumeController for Admin", description = "어드민 전용 판매 이력서 조회 컨트롤러")

public class ResumeAdminController {

    private final ResumeService resumeService;

    @Operation(summary = "요청 Resume 상세 조회 API", description = "요청 Resume 상세 조회 API")
    @GetMapping("{resumeId}")
    public Api<ResumeAdminDetailResponse> retrieve(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long resumeId
    ){
        Resume resume = resumeService.getByIdWithThrow(currentUser, resumeId);
        return Api.OK(ResumeAdminDetailResponse.from(resume));
    }

    @Operation(summary = "전체 요청 Resume 조회 API",
            description = """
                    sortType 정렬조건[OLD, NEW, HIGHEST_PRICE, LOWEST_PRICE, VIEW_COUNT, BEST_SELLING, DEFAULT]
                     minPrice, maxPrice 가격 범위 필터링 조건
                     분야 조건 : field [FRONTEND BACKEND ANDROID IOS DEVOPS AI ETC UNSELECTED]
                     년차 조건 : level [NEW, JUNIOR, SENIOR]
                     pageStep : [FIRST(default), NEXT, PREVIOUS, LAST]
                     lastModifiedAt : 이전, 다음 페이지 호출시 반드시 필요
                     limit 가져올 개수 기본 6개
                     lastId : 이전, 다음 페이지 호출시 반드시 필요
                    """)
    @GetMapping("/page")
    public Api<ResumePage> getFilteredPage(
            @RequestParam(required = false) PeriodCond periodCond,
            @RequestParam(required = false) ResumeStatus status,
            @RequestParam(defaultValue = "FIRST") PageStep pageStep,
            @RequestParam(required = false) LocalDateTime lastModifiedAt,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "6") int limit
    ) {
        // 필터링 및 페이징을 처리하는 서비스 호출
        ResumePage result = resumeService
                .getFilteredAndPagedResults(periodCond, status, pageStep, lastModifiedAt, lastId, limit);
        return Api.OK(result);
    }
}
