package project.forwork.api.domain.retrylog.service.port;

import project.forwork.api.domain.retrylog.model.RetryLog;

public interface RetryLogRepository {
    RetryLog save(RetryLog retryLog);
}
