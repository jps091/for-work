package project.forwork.api.domain.salespost.service.port;

import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.model.SalesPost;

import java.util.Optional;

public interface SalesPostRepository {
    SalesPost save(SalesPost salesPost);
    SalesPost getByIdWithThrow(Long salesPostId);
    SalesPost getByIdWithPessimisticLock(Long salesPostId);
    SalesPost getByIdWithOptimisticLock(Long salesPostId);
    Optional<SalesPost> findById(Long salesPostId);
    SalesPost getByResumeIdWithThrow(Long resumeId);
    Optional<SalesPost> findByResumeId(Long resumeId);
    void deleteByResumeId(Long resumeId);
}
