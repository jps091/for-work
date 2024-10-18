package project.forwork.api.domain.retrylog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.domain.retrylog.infrastructure.enums.RetryType;
import project.forwork.api.domain.retrylog.model.RetryLog;
import project.forwork.api.domain.retrylog.service.port.RetryLogRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryLogService {

    private final RetryLogRepository retryLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void register(String requestId, RetryType type, Exception e) {

        project.forwork.api.domain.retrylog.model.RetryLog retryLog = RetryLog.builder()
                .requestId(requestId)
                .errorResponse(e.getMessage())
                .type(type)
                .build();
        retryLogRepository.save(retryLog);
    }
}
