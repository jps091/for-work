package project.forwork.api.mock;

import project.forwork.api.common.error.SalesPostErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.salespost.model.SalesPost;
import project.forwork.api.domain.salespost.service.port.SalesPostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeSalesPostRepository implements SalesPostRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<SalesPost> data = new ArrayList<>();


    @Override
    public SalesPost save(SalesPost salesPost) {
        if(salesPost.getId() == null || salesPost.getId() == 0){
            SalesPost newSalesPost = SalesPost.builder()
                    .id(id.incrementAndGet())
                    .resume(salesPost.getResume())
                    .thumbnailImage(salesPost.getThumbnailImage())
                    .salesStatus(salesPost.getSalesStatus())
                    .registeredAt(salesPost.getRegisteredAt())
                    .build();
            data.add(newSalesPost);
            return newSalesPost;
        }else{
            data.removeIf(r -> Objects.equals(r.getId(), salesPost.getId()));
            data.add(salesPost);
            return salesPost;
        }
    }

    @Override
    public SalesPost getByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(SalesPostErrorCode.SALES_POST_NOT_FOUND, id));
    }

    @Override
    public Optional<SalesPost> findById(Long id) {
        return data.stream().filter(s -> Objects.equals(s.getId(), id)).findAny();
    }

    @Override
    public SalesPost getByResumeWithThrow(Resume resume) {
        return data.stream()
                .filter(s -> Objects.equals(s.getResume().getId(), resume.getId()))
                .findAny()
                .orElseThrow(() -> new ApiException(SalesPostErrorCode.SALES_POST_NOT_FOUND));
    }

    @Override
    public Optional<SalesPost> findByResume(Resume resume) {
        return data.stream()
                .filter(s -> Objects.equals(s.getResume().getId(), resume.getId()))
                .findAny();
    }

    @Override
    public SalesPost getByIdWithPessimisticLock(Long salesPostId) {
        return findById(salesPostId).orElseThrow(() -> new ApiException(SalesPostErrorCode.SALES_POST_NOT_FOUND));
    }

    @Override
    public SalesPost getByIdWithOptimisticLock(Long salesPostId) {
        return findById(salesPostId).orElseThrow(() -> new ApiException(SalesPostErrorCode.SALES_POST_NOT_FOUND));
    }

    @Override
    public void deleteByResumeId(Long resumeId) {
        data.removeIf(s -> Objects.equals(s.getResume().getId(), resumeId));
    }
}