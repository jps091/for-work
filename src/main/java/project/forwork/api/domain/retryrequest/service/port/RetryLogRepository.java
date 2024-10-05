package project.forwork.api.domain.retryrequest.service.port;

import project.forwork.api.domain.retryrequest.model.RetryLog;

public interface RetryLogRepository {
    RetryLog save(RetryLog retryLog);
}
