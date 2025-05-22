package project.forwork.api.domain.resume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.controller.model.ResumeAdminResponse;
import project.forwork.api.domain.resume.controller.model.ResumePage;
import project.forwork.api.domain.resume.controller.model.ResumeSellerResponse;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeQueryPort;
import project.forwork.api.domain.resume.service.port.ResumeViewPort;


import java.util.List;

@Service
@Builder
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResumeQueryService {

    private final ResumeViewPort resumeViewPort;
    private final ResumeQueryPort resumeQueryPort;

    public Resume getByIdWithThrow(CurrentUser currentUser, Long resumeId){
        Resume resume = resumeQueryPort.getByIdWithThrow(resumeId);
        validateAuthorOrAdmin(currentUser, resume);
        return resume;
    }

    public List<ResumeSellerResponse> findResumesBySeller(CurrentUser currentUser){
        List<ResumeStatus> statusList = List.of(ResumeStatus.ACTIVE, ResumeStatus.PENDING, ResumeStatus.REJECTED);
        List<ResumeSellerResponse> resumeAdminResponses = resumeQueryPort.findAllBySeller(currentUser.getId(), statusList)
                .stream()
                .map(ResumeSellerResponse::from)
                .toList();

        if(resumeAdminResponses.isEmpty()){
            throw new ApiException(ResumeErrorCode.RESUME_NO_CONTENT);
        }

        return resumeAdminResponses;
    }

    public ResumePage findFirstPage(
            PeriodCond periodCond, ResumeStatus status, int limit
    ){
        List<ResumeAdminResponse> results = resumeViewPort.findFirstPage(periodCond, status, limit + 1);
        if(results.size() <= limit){
            return createResumePage(results, true, true);
        }
        results = results.subList(0, limit);
        return createResumePage(results, true, false);
    }

    public ResumePage findLastPage(
            PeriodCond periodCond, ResumeStatus status, int limit
    ){
        List<ResumeAdminResponse> results = resumeViewPort.findLastPage(periodCond, status, limit);
        return createResumePage(results, false, true);
    }

    public ResumePage findNextPage(
            PeriodCond periodCond, ResumeStatus status,
            Long lastId, int limit
    ){
        List<ResumeAdminResponse> results = resumeViewPort.findNextPage(periodCond, status, lastId, limit + 1);
        validResultIsEmpty(results);
        boolean isLastPage = results.size() <= limit; // limit + 1과 비교하여 마지막 페이지 여부 판단
        if (!isLastPage) {
            results = results.subList(0, limit);
        }

        return createResumePage(results, false, isLastPage);
    }

    public ResumePage findPreviousPage(
            PeriodCond periodCond, ResumeStatus status,
            Long lastId, int limit
    ){
        List<ResumeAdminResponse> results = resumeViewPort.findPreviousPage(periodCond, status, lastId, limit + 1);
        boolean isFirstPage = results.size() <= limit; // limit + 1과 비교하여 마지막 페이지 여부 판단
        if (!isFirstPage) {
            results = results.subList(1, limit + 1);
        }

        return createResumePage(results, isFirstPage, false);
    }

    public ResumePage createResumePage(List<ResumeAdminResponse> results, boolean isFirstPage, boolean isLastPage){
        validResultIsEmpty(results);
        return ResumePage.from(results, isFirstPage, isLastPage);
    }

    private static void validateAuthorOrAdmin(CurrentUser currentUser, Resume resume) {
        if(currentUser.isAdminMismatch() && resume.isAuthorMismatch(currentUser.getId())){
            throw new ApiException(ResumeErrorCode.ACCESS_NOT_PERMISSION);
        }
    }

    private static void validResultIsEmpty(List<ResumeAdminResponse> result) {
        if(result.isEmpty()){
            throw new ApiException(ResumeErrorCode.RESUME_NO_CONTENT);
        }
    }
}
