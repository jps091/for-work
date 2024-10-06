package project.forwork.api.domain.retrylog.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.retrylog.model.RetryLog;
import project.forwork.api.domain.retrylog.service.port.RetryLogRepository;

@Repository
@RequiredArgsConstructor
public class RetryLogRepositoryImpl implements RetryLogRepository {

    private final RetryLogJpaRepository retryLogJpaRepository;

    @Override
    public RetryLog save(RetryLog retryLog) {
        return retryLogJpaRepository.save(RetryLogEntity.from(retryLog)).toModel();
    }
}
