package project.forwork.api.domain.maillog.infrastructure;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.maillog.infrastructure.enums.Result;
import project.forwork.api.domain.maillog.model.MailLog;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;

@Entity
@Table(name = "mail_logs")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailLogEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_log_id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "request_id", nullable = false)
    private String requestId;

    @Column(name = "resume_id", nullable = false)
    private Long resumeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Result result;

    @Column(name = "error_response")
    private String errorResponse;

    public static MailLogEntity from(MailLog mailLog){
        MailLogEntity mailLogEntity = new MailLogEntity();
        mailLogEntity.id = mailLog.getId();
        mailLogEntity.email = mailLog.getEmail();
        mailLogEntity.requestId = mailLog.getRequestId();
        mailLogEntity.resumeId = mailLog.getResumeId();
        mailLogEntity.result = mailLog.getResult();
        mailLogEntity.errorResponse = mailLog.getErrorResponse();
        return  mailLogEntity;
    }

    public MailLog toModel(){
        return MailLog.builder()
                .id(id)
                .email(email)
                .requestId(requestId)
                .resumeId(resumeId)
                .result(result)
                .errorResponse(errorResponse)
                .build();
    }
}
