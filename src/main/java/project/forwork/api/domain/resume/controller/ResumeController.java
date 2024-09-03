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
import project.forwork.api.domain.resume.service.ResumeService;
import project.forwork.api.common.domain.CurrentUser;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
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
        ResumeDetailResponse resumeDetailResponse = resumeService.register(currentUser, resumeRegisterRequest);
        return Api.CREATED(resumeDetailResponse);
    }


    @Operation(summary = "Resume 삭제 API", description = "ResumeId 입력")
    @DeleteMapping("{resumeId}")
    public Api<String> delete(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @Parameter(description = "삭제할 Resume ID", required = true, example = "1")
            @PathVariable Long resumeId
    ){
        resumeService.delete(currentUser, resumeId);
        return Api.OK("Resume 삭제 성공");
    }

    @Operation(summary = "Resume 수정 API", description = "ResumeId 입력")
    @PutMapping("{resumeId}")
    public Api<String> edit(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @Parameter(description = "수정할 Resume ID", required = true, example = "1")
            @PathVariable Long resumeId,
            @Valid @RequestBody ResumeModifyRequest resumeModifyRequest
    ){
        resumeService.modify(resumeId, currentUser, resumeModifyRequest);
        return Api.OK("Resume 변경 완료 되었습니다.");
    }

    @Operation(summary = "회원Resume 조회 API", description = "로그인한 회원의 전체 Resume 조회")
    @GetMapping("{resumeId}")
    public Api<ResumeDetailResponse> retrieve(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long resumeId
    ){
        ResumeDetailResponse resume = resumeService.getByIdWithThrow(currentUser, resumeId);
        return Api.OK(resume);
    }

    @Operation(summary = "회원Resume 조회 API", description = "로그인한 회원의 전체 Resume 조회")
    @GetMapping
    public Api<List<ResumeResponse>> retrieveAll(
            @Parameter(hidden = true) @Current CurrentUser currentUser
    ){
        List<ResumeResponse> resumes = resumeService.findResumesBySeller(currentUser);
        return Api.OK(resumes);
    }
}
