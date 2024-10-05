package project.forwork.api.domain.retryrequest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.retryrequest.infrastructure.enums.RetryType;

@AllArgsConstructor
@Builder
@Getter
public class RetryLog {
    private final Long id;
    private final String requestId;
    private final String errorResponse;
    private final RetryType type;
}
