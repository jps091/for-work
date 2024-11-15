package project.forwork.api.domain.maillog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.maillog.infrastructure.enums.EmailType;

@Builder
@AllArgsConstructor
@Getter
public class MailLog {
    private final Long id;
    private final String email;
    private final String requestId;
    private final Long resumeId;
    private final EmailType emailType;
    private final String errorResponse;
    private final String messageContent;

    public static MailLog create(String email, String requestId, Long resumeId, Exception e){
        return MailLog.builder()
                .email(email)
                .requestId(requestId)
                .resumeId(resumeId)
                .emailType(EmailType.BUYER)
                .errorResponse(e.getMessage())
                .build();
    }
}
