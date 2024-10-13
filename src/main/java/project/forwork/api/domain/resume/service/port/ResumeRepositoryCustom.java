package project.forwork.api.domain.resume.service.port;

import project.forwork.api.domain.resume.controller.model.ResumeResponse;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;

import java.time.LocalDateTime;
import java.util.List;

/***
 * index : modifiedat_status_id, modifiedat_id, status_modifiedat_id
 */
public interface ResumeRepositoryCustom {
    List<ResumeResponse> findFirstPage(PeriodCond periodCond, ResumeStatus status, int limit);
    List<ResumeResponse> findLastPage(PeriodCond periodCond, ResumeStatus status, int limit);
    List<ResumeResponse> findNextPage(PeriodCond periodCond, ResumeStatus status, LocalDateTime lastModifiedAt, Long lastId, int limit);
    List<ResumeResponse> findPreviousPage(PeriodCond periodCond, ResumeStatus status, LocalDateTime lastModifiedAt, Long lastId, int limit);
}
