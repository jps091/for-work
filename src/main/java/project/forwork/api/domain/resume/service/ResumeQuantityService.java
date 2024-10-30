package project.forwork.api.domain.resume.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ResumeQuantityService {

    private final ResumeRepository resumeRepository;

    public void addSalesQuantityWithAllPessimistic(List<Long> resumeIds){
        List<Resume> resumes = resumeRepository.findByIdsWithPessimisticLock(resumeIds).stream()
                .map(Resume::increaseSalesQuantity)
                .toList();
        resumeRepository.saveAll(resumes);
    }

    public void addSalesQuantityWithOnePessimistic(List<Long> resumeIds){
        for (Long resumeId : resumeIds) {
            Resume resume = resumeRepository.getByIdWithPessimisticLock(resumeId);
            resume = resume.increaseSalesQuantity();
            resumeRepository.save(resume);
        }
    }

    public void addSalesQuantityWithUpdate(List<Long> resumeIds){
        for (Long resumeId : resumeIds) {
            resumeRepository.increaseQuantity(resumeId);
        }
    }

    /*** TODO 배포시 주석 삭제 필요
     * Hibernate -> ObjectOptimisticLockingFailureException, StaleObjectStateException 발생
     * Spring은 ObjectOptimisticLockingFailureException 예외로 감싸서 반환
     * 주의 : 긍적적 락은 mysql에는 없는 개념 (그냥 update 하기전에 확인)
     *        처음 요청 순이 아니라 롤백하고 다시 재요청 순으로 결정됨 (선착순 적용X)
     */
    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3000,
            backoff =  @Backoff(delay = 1000)
    )
    public void addSalesQuantityWithOptimistic(List<Long> resumeIds){

        List<Resume> resumes = resumeIds.stream()
                .map(resumeRepository::getByIdWithOptimisticLock)
                .map(Resume::increaseSalesQuantity)
                .toList();
        resumeRepository.saveAll(resumes);
    }
}
