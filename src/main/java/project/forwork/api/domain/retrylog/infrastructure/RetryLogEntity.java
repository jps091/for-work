package project.forwork.api.domain.retrylog.infrastructure;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.retrylog.infrastructure.enums.RetryType;
import project.forwork.api.domain.retrylog.model.RetryLog;

@Entity
@Table(name = "retry_logs")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RetryLogEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retry_log_id")
    private Long id;

    @Column(name = "request_id", nullable = false)
    private String requestId;

    @Column(name = "error_response", nullable = false)
    private String errorResponse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RetryType type;

    public static RetryLogEntity from(RetryLog retryLog){
        RetryLogEntity retryLogEntity = new RetryLogEntity();
        retryLogEntity.id = retryLog.getId();
        retryLogEntity.requestId = retryLog.getRequestId();
        retryLogEntity.errorResponse = retryLog.getErrorResponse();
        retryLogEntity.type = retryLog.getType();
        return retryLogEntity;
    }

    public RetryLog toModel(){
        return RetryLog.builder()
                .id(id)
                .requestId(requestId)
                .errorResponse(errorResponse)
                .type(type)
                .build();
    }
}
