package project.forwork.api.domain.resume.service.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.forwork.api.domain.resume.controller.model.ResumeAdminResponse;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.infrastructure.querydsl.ResumeSearchCond;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resumedecision.controller.model.ResumeDecisionResponse;
import project.forwork.api.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository {

    Resume save(Resume resume);

    void delete(Resume resume);

    Resume getByIdWithThrow(Long id);

    Optional<Resume> findById(Long id);

    List<Resume> findAll();

    List<Resume> findAllByStatus(ResumeStatus status);

    List<Resume> findAllBySeller(User seller);

    Page<ResumeAdminResponse> search(ResumeSearchCond cond, PageRequest pageRequest);
}
