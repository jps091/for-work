package project.forwork.api.domain.resume.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.resume.controller.model.ResumeModifyRequest;
import project.forwork.api.domain.resume.controller.model.ResumeRegisterRequest;
import project.forwork.api.domain.resume.controller.model.ResumeDetailResponse;
import project.forwork.api.domain.resume.controller.model.ResumeResponse;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.ResumeService;
import project.forwork.api.common.domain.CurrentUser;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
@Tag(name = "ResumeApiController", description = "Resume Api 서비스 컨트롤러")
public class ResumeController {

    private final ResumeService resumeService;

    @Operation(summary = "Resume 생성", description = "Resume 생성")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public Api<ResumeDetailResponse> register(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @Valid @RequestBody ResumeRegisterRequest resumeRegisterRequest
    ){
        Resume resume = resumeService.register(currentUser, resumeRegisterRequest);
        return Api.CREATED(ResumeDetailResponse.from(resume));
    }


    @Operation(summary = "Resume 삭제 API", description = "ResumeId 입력")
    @DeleteMapping("{resumeId}")
    public Api<String> delete(
            @Parameter(description = "삭제할 Resume ID", required = true, example = "1")
            @PathVariable Long resumeId,
            @Parameter(hidden = true) @Current CurrentUser currentUser
    ){
        resumeService.delete(resumeId, currentUser);
        return Api.OK("Resume 삭제 성공");
    }

    @Operation(summary = "Resume 수정 API",
            description = "Resume 수정시 상태가 PENDING 으로 변경 / 만약 판매글도 존재 할시 판매글 상태 CANCEL 으로 변경")
    @PutMapping("{resumeId}")
    public Api<String> modifyIfPending(
            @Parameter(description = "수정할 Resume ID", required = true, example = "1")
            @PathVariable Long resumeId,
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @Valid @RequestBody ResumeModifyRequest resumeModifyRequest
    ){
        resumeService.modify(resumeId, currentUser, resumeModifyRequest);
        return Api.OK("Resume 변경 완료 되었습니다.");
    }

/*    @Operation(summary = "Resume Pending 상태  API", description = "api 호출시 Resume 상태 Pending으로 변경")
    @PutMapping("/status/{resumeId}")
    public Api<String> updatePending(
            @Parameter(description = "요청 Resume ID", required = true, example = "1")
            @PathVariable Long resumeId,
            @Parameter(hidden = true) @Current CurrentUser currentUser
    ){
        resumeService.updatePending(resumeId, currentUser);
        return Api.OK("Resume 상태 Pending 변경 완료 되었습니다.");
    }*/

    @Operation(summary = "자신의 Resume 단건 조회 API", description = "로그인한 회원의 전체 Resume 조회")
    @GetMapping("{resumeId}")
    public Api<ResumeDetailResponse> retrieve(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long resumeId
    ){
        Resume resume = resumeService.getByIdWithThrow(currentUser, resumeId);
        return Api.OK(ResumeDetailResponse.from(resume));
    }

    @Operation(summary = "자신의 Resume 전체조회 API", description = "로그인한 회원의 전체 Resume 조회")
    @GetMapping
    public Api<List<ResumeResponse>> retrieveAll(
            @Parameter(hidden = true) @Current CurrentUser currentUser
    ){
        List<ResumeResponse> resumes = resumeService.findResumesBySeller(currentUser);
        return Api.OK(resumes);
    }
}
