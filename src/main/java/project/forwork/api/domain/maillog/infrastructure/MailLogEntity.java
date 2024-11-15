package project.forwork.api.domain.maillog.infrastructure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.maillog.infrastructure.enums.EmailType;
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

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "type")
    private EmailType emailType;

    @Column(length = 255, name = "error_response")
    private String errorResponse;

    @Column(length = 300, name = "message_content")
    private String messageContent;

    public static MailLogEntity from(MailLog mailLog){
        MailLogEntity mailLogEntity = new MailLogEntity();
        mailLogEntity.id = mailLog.getId();
        mailLogEntity.email = mailLog.getEmail();
        mailLogEntity.messageContent = mailLog.getMessageContent();
        mailLogEntity.emailType = mailLog.getEmailType();
        mailLogEntity.errorResponse = mailLog.getErrorResponse();
        return  mailLogEntity;
    }

    public MailLog toModel(){
        return MailLog.builder()
                .id(id)
                .email(email)
                .messageContent(messageContent)
                .emailType(emailType)
                .errorResponse(errorResponse)
                .build();
    }
}
