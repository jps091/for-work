package project.forwork.api.domain.resume.service.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.forwork.api.domain.resume.controller.model.ResumeResponse;
import project.forwork.api.domain.resume.infrastructure.ResumeSearchCond2;
import project.forwork.api.domain.resume.infrastructure.enums.PeriodCond;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ResumeRepositoryCustom {
    Page<ResumeResponse> search(ResumeSearchCond2 cond, Pageable pageable);
    List<ResumeResponse> findFirstPage(PeriodCond periodCond, ResumeStatus status, int limit);
    List<ResumeResponse> findLastPage(PeriodCond periodCond, ResumeStatus status, int limit);
    List<ResumeResponse> findNextPage(PeriodCond periodCond, ResumeStatus status, LocalDateTime lastModifiedAt, Long lastId, int limit);
    List<ResumeResponse> findPreviousPage(PeriodCond periodCond, ResumeStatus status, LocalDateTime lastModifiedAt, Long lastId, int limit);
}
