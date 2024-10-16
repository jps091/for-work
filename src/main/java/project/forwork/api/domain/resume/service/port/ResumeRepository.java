package project.forwork.api.domain.resume.service.port;

import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository {

    Resume save(Resume resume);

    void delete(Resume resume);

    Resume getByIdWithThrow(Long id);

    Optional<Resume> findById(Long id);

    List<Resume> findAllBySeller(User seller);

    List<Resume> getByIdsWithPessimisticLock(List<Long> resumeIds);

    Resume getByIdWithPessimisticLock(Long resumeId);

    Resume getByIdWithOptimisticLock(Long resumeId);

    List<Resume> saveAll(List<Resume> resumes);
}
