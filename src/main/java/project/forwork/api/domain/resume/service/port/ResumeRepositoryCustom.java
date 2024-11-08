package project.forwork.api.domain.resume.service.port;

import project.forwork.api.domain.resume.controller.model.ResumeAdminResponse;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;

import java.time.LocalDateTime;
import java.util.List;

/***
 * index : modifiedat_status_id, modifiedat_id, status_modifiedat_id
 */
public interface ResumeRepositoryCustom {
    List<ResumeAdminResponse> findFirstPage(PeriodCond periodCond, ResumeStatus status, int limit);
    List<ResumeAdminResponse> findLastPage(PeriodCond periodCond, ResumeStatus status, int limit);
    List<ResumeAdminResponse> findNextPage(PeriodCond periodCond, ResumeStatus status, Long lastId, int limit);
    List<ResumeAdminResponse> findPreviousPage(PeriodCond periodCond, ResumeStatus status, Long lastId, int limit);
}
