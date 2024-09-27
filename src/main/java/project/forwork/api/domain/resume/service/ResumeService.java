package project.forwork.api.domain.resume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.controller.model.*;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.domain.resume.service.port.ResumeRepositoryCustom;
import project.forwork.api.domain.resumedecision.service.port.ResumeDecisionRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Builder
@Slf4j
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeDecisionRepository resumeDecisionRepository;
    private final ResumeRepositoryCustom resumeRepositoryCustom;
    private final UserRepository userRepository;

    public Resume register(CurrentUser currentUser, ResumeRegisterRequest body){
        User user = userRepository.getByIdWithThrow(currentUser.getId());
        Resume resume = Resume.from(user, body);
        resume =  resumeRepository.save(resume);
        return resume;
    }

    public void modifyResumePending(
            Long resumeId,
            CurrentUser currentUser,
            ResumeModifyRequest body
    ){
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        validateAuthor(currentUser, resume);

        resume = resume.modifyResumePending(body);
        resumeRepository.save(resume);
    }

    public void updatePending(Long resumeId, CurrentUser currentUser){
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        validateAuthor(currentUser, resume);

        resume = resume.updateStatus(ResumeStatus.PENDING);
        resumeRepository.save(resume);
    }

    public void delete(Long resumeId, CurrentUser currentUser) {
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);
        validateAuthor(currentUser, resume);

        resumeRepository.delete(resume);
    }

    public Resume getByIdWithThrow(CurrentUser currentUser, Long resumeId){
        Resume resume = resumeRepository.getByIdWithThrow(resumeId);

        validateAuthorOrAdmin(currentUser, resume);
        return resume;
    }

    public ResumePage findFirstPage(
            PeriodCond periodCond, ResumeStatus status, int limit
    ){
        List<ResumeResponse> results = resumeRepositoryCustom.findFirstPage(periodCond, status, limit);
        return createResumePage(results);
    }

    public ResumePage findLastPage(
            PeriodCond periodCond, ResumeStatus status, int limit
    ){
        List<ResumeResponse> results = resumeRepositoryCustom.findLastPage(periodCond, status, limit);
        return createResumePage(results);
    }

    public ResumePage findNextPage(
            PeriodCond periodCond, ResumeStatus status,
            LocalDateTime lastModifiedAt, Long lastId, int limit
    ){
        List<ResumeResponse> results = resumeRepositoryCustom.findNextPage(periodCond, status, lastModifiedAt, lastId, limit);

        if(results.isEmpty()){
            throw new ApiException(ResumeErrorCode.RESUME_NO_CONTENT);
        }

        return createResumePage(results);
    }

    public ResumePage findPreviousPage(
            PeriodCond periodCond, ResumeStatus status,
            LocalDateTime lastModifiedAt, Long lastId, int limit
    ){
        List<ResumeResponse> results = resumeRepositoryCustom.findPreviousPage(periodCond, status, lastModifiedAt, lastId, limit);
        log.info("results size={}",results.size());
        return createResumePage(results);
    }

    public List<ResumeResponse> findResumesBySeller(CurrentUser currentUser){
        User user = userRepository.getByIdWithThrow(currentUser.getId());

        List<ResumeResponse> resumeResponses = resumeRepository.findAllBySeller(user)
                .stream()
                .map(ResumeResponse::from)
                .toList();

        if(resumeResponses.isEmpty()){
            throw new ApiException(ResumeErrorCode.RESUME_NO_CONTENT);
        }

        return resumeResponses;
    }

    private static void validateAuthor(CurrentUser currentUser, Resume resume) {
        if(resume.isAuthorMismatch(currentUser.getId())){
            throw new ApiException(ResumeErrorCode.ACCESS_NOT_PERMISSION);
        }
    }

    private static void validateAuthorOrAdmin(CurrentUser currentUser, Resume resume) {
        if(currentUser.isAdminMismatch() && resume.isAuthorMismatch(currentUser.getId())){
            throw new ApiException(ResumeErrorCode.ACCESS_NOT_PERMISSION);
        }
    }

    private static ResumePage createResumePage(List<ResumeResponse> results) {
        ResumeResponse lastRecord = results.get(results.size() - 1);

        return ResumePage.builder()
                .lastId(lastRecord.getId())
                .lastModifiedAt(lastRecord.getModifiedAt())
                .results(results)
                .build();
    }
}
