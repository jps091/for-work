package project.forwork.api.mock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.controller.model.ResumeResponse;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.infrastructure.querydsl.ResumeSearchCond;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

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
                    .resumeUrl(resume.getResumeUrl())
                    .architectureImageUrl(resume.getArchitectureImageUrl())
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
    public void deleteAllBySeller(User Seller) {
        //
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
    public List<Resume> findAll() {
        return data;
    }

    @Override
    public List<Resume> findAllByStatus(ResumeStatus status) {
        return null; //
    }

    @Override
    public List<Resume> findAllBySeller(User seller) {
        return data.stream().filter(r -> Objects.equals(r.getSeller(), seller)).toList();
    }

    @Override
    public Page<ResumeResponse> search(ResumeSearchCond cond, PageRequest pageRequest) {
        return null; //querydsl
    }
}