package project.forwork.api.domain.resume.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.forwork.api.common.annotation.Current;
import project.forwork.api.common.api.Api;
import project.forwork.api.domain.resume.controller.model.*;
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
    @PostMapping(value = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Api<String > register(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @RequestPart("resumeData") @Valid ResumeRegisterRequest resumeRegisterBody,
            @RequestPart("descriptionImage") @NotNull MultipartFile file
    ){
        Resume resume = resumeService.register(currentUser, resumeRegisterBody, file);
        return Api.CREATED(resume.createTitle() +" 등록 완료");
    }


    @Operation(summary = "Resume 삭제 API", description = "ResumeId 입력")
    @DeleteMapping("/{resumeId}")
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
    @PutMapping(value = "/{resumeId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Api<String> modifyIfPending(
            @Parameter(description = "수정할 Resume ID", required = true, example = "1")
            @PathVariable Long resumeId,
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @RequestPart("resumeData") @Valid ResumeModifyRequest body,
            @RequestPart("descriptionImage") @Valid @NotNull MultipartFile file
    ){
        resumeService.modify(resumeId, currentUser, body, file);
        return Api.OK("Resume 변경 완료 되었습니다.");
    }

    @Operation(summary = "자신의 Resume 단건 조회 API", description = "단건 Resume 조회")
    @GetMapping("/{resumeId}")
    public Api<ResumeSellerDetailResponse> retrieve(
            @Parameter(hidden = true) @Current CurrentUser currentUser,
            @PathVariable Long resumeId
    ){
        Resume resume = resumeService.getByIdWithThrow(currentUser, resumeId);
        return Api.OK(ResumeSellerDetailResponse.from(resume));
    }

    @Operation(summary = "자신의 Resume 전체조회 API", description = "로그인한 회원의 전체 Resume 조회")
    @GetMapping
    public Api<List<ResumeSellerResponse>> retrieveAll(
            @Parameter(hidden = true) @Current CurrentUser currentUser
    ){
        List<ResumeSellerResponse> resumes = resumeService.findResumesBySeller(currentUser);
        return Api.OK(resumes);
    }
}