package project.forwork.api.mock;

import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeResumeRepository implements ResumeRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<Resume> data = new ArrayList<>();


    @Override
    public Resume save(Resume resume) {
        if(resume.getId() == null || resume.getId() == 0){
            Resume newResume = Resume.builder()
                    .id(id.incrementAndGet())
                    .seller(resume.getSeller())
                    .field(resume.getField())
                    .level(resume.getLevel())
                    .registeredAt(resume.getRegisteredAt())
                    .salesQuantity(resume.getSalesQuantity())
                    .resumeUrl(resume.getResumeUrl())
                    .descriptionImageUrl(resume.getDescriptionImageUrl())
                    .price(resume.getPrice())
                    .description(resume.getDescription())
                    .status(ResumeStatus.PENDING)
                    .build();
            data.add(newResume);
            return newResume;
        }else{
            data.removeIf(r -> Objects.equals(r.getId(), resume.getId()));
            data.add(resume);
            return resume;
        }
    }

    @Override
    public void delete(Resume resume) {
        data.removeIf(r -> Objects.equals(r.getId(), resume.getId()));
    }

    @Override
    public Resume getByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(ResumeErrorCode.RESUME_NOT_FOUND, id));
    }

    @Override
    public Optional<Resume> findById(Long id) {
        return data.stream().filter(r -> Objects.equals(r.getId(), id)).findAny();
    }

    @Override
    public List<Resume> findAllBySeller(Long userId, List<ResumeStatus> statusList) {
        return data.stream().filter(r -> Objects.equals(r.getSeller().getId(), userId) &&
                        !Objects.equals(r.getStatus(), ResumeStatus.DELETE))
                        .toList();
    }

    @Override
    public List<Resume> findByIdsWithPessimisticLock(List<Long> resumeIds) {
        return data.stream()
                .filter(resume -> resumeIds.contains(resume.getId())).toList();
    }

    @Override
    public Resume getByIdWithPessimisticLock(Long resumeId) {
        return findById(resumeId).orElseThrow(() -> new ApiException(SalesPostErrorCode.SALES_POST_NOT_FOUND));
    }

    @Override
    public Resume getByIdWithOptimisticLock(Long resumeId) {
        return findById(resumeId).orElseThrow(() -> new ApiException(SalesPostErrorCode.SALES_POST_NOT_FOUND));
    }

    @Override
    public List<Resume> saveAll(List<Resume> resumes) {
        return resumes.stream().map(this::save).toList();
    }


    @Override
    public List<Resume> findByIds(List<Long> resumeIds) {
        return data.stream()
                .filter(resume -> resumeIds.contains(resume.getId())).toList();
    }
}