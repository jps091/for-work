package project.forwork.api.domain.salespost.service.port;

import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.model.SalesPost;

import java.util.Optional;

public interface SalesPostRepository {

    SalesPost save(SalesPost salesPost);

    SalesPost getByIdWithThrow(Long salesPostId);

    Optional<SalesPost> findById(Long salesPostId);

    SalesPost getSellingPostWithThrow(Long resumeId);

    SalesPost getByResumeWithThrow(Resume resume);
}
