package project.forwork.api.domain.retrylog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import project.forwork.api.domain.retrylog.infrastructure.enums.RetryType;

@AllArgsConstructor
@Builder
@Getter
@Data
public class RetryLog {
    private final Long id;
    private final String requestId;
    private final String errorResponse;
    private final RetryType type;
}