package project.forwork.api.domain.resume.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.controller.model.ResumeAdminResponse;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.service.port.ResumeRepositoryCustom;


import java.util.List;

@Service
@Builder
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResumePageService {

    private final ResumeRepositoryCustom resumeRepositoryCustom;

    public List<ResumeAdminResponse> findFirstPage(
            PeriodCond periodCond, ResumeStatus status, int limit
    ){
        List<ResumeAdminResponse> result = resumeRepositoryCustom.findFirstPage(periodCond, status, limit);
        validResultIsEmpty(result);
        return result;
    }

    public List<ResumeAdminResponse> findLastPage(
            PeriodCond periodCond, ResumeStatus status, int limit
    ){
        List<ResumeAdminResponse> result = resumeRepositoryCustom.findLastPage(periodCond, status, limit);
        validResultIsEmpty(result);
        return result;
    }

    public List<ResumeAdminResponse> findNextPage(
            PeriodCond periodCond, ResumeStatus status,
            Long lastId, int limit
    ){
        List<ResumeAdminResponse> result = resumeRepositoryCustom.findNextPage(periodCond, status, lastId, limit);
        validResultIsEmpty(result);
        return result;
    }

    public List<ResumeAdminResponse> findPreviousPage(
            PeriodCond periodCond, ResumeStatus status,
            Long lastId, int limit
    ){
        List<ResumeAdminResponse> result = resumeRepositoryCustom.findPreviousPage(periodCond, status, lastId, limit);
        validResultIsEmpty(result);
        return result;
    }

    private static void validResultIsEmpty(List<ResumeAdminResponse> result) {
        if(result.isEmpty()){
            throw new ApiException(ResumeErrorCode.RESUME_NO_CONTENT);
        }
    }
}
