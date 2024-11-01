package project.forwork.api.domain.maillog.infrastructure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.maillog.infrastructure.enums.Result;
import project.forwork.api.domain.maillog.model.MailLog;

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

    @Column(length = 50, name = "email") @NotNull
    private String email;

    @Column(length = 25, name = "request_id") @NotNull
    private String requestId;

    @Column(name = "resume_id") @NotNull
    private Long resumeId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Result result;

    @Column(length = 255, name = "error_response") // char
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
