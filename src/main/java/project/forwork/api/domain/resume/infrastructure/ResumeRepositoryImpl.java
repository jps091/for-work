package project.forwork.api.domain.resume.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.controller.model.ResumeResponse;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.infrastructure.querydsl.ResumeQueryDlsRepository;
import project.forwork.api.domain.resume.infrastructure.querydsl.ResumeSearchCond;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.user.infrastructure.UserEntity;
import project.forwork.api.domain.user.model.User;

import java.util.List;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class ResumeRepositoryImpl implements ResumeRepository {

    private final ResumeJpaRepository resumeJpaRepository;
    private final ResumeQueryDlsRepository resumeQueryDlsRepository;


    @Override
    public Resume save(Resume resume) {
        return resumeJpaRepository.save(ResumeEntity.from(resume)).toModel();
    }

    @Override
    public void delete(Resume resume) {
        resumeJpaRepository.delete(ResumeEntity.from(resume));
    }

    @Override
    public void deleteAllBySeller(User seller) {
        List<ResumeEntity> resumeEntityList = resumeJpaRepository.findAllBySellerEntity(UserEntity.from(seller));
        resumeJpaRepository.deleteAll(resumeEntityList);
    }

    @Override
    public Resume getByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(ResumeErrorCode.RESUME_NOT_FOUND, id));
    }

    @Override
    public Optional<Resume> findById(Long id) {
        return resumeJpaRepository.findById(id).map(ResumeEntity::toModel);
    }

    @Override
    public List<Resume> findAll() {
        return resumeJpaRepository
                .findAll()
                .stream()
                .map(ResumeEntity::toModel)
                .toList();
    }

    @Override
    public List<Resume> findAllByStatus(ResumeStatus status) {
        return resumeJpaRepository
                .findByResumeStatus(status)
                .stream()
                .map(ResumeEntity::toModel)
                .toList();
    }

    @Override
    public List<Resume> findAllBySeller(User seller) {
        return resumeJpaRepository.findAllBySellerEntity(UserEntity.from(seller))
                .stream()
                .map(ResumeEntity::toModel)
                .toList();
    }

    @Override
    public Page<ResumeResponse> search(ResumeSearchCond cond, PageRequest pageRequest) {
        return resumeQueryDlsRepository.search(cond, pageRequest);
    }
}
