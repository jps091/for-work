package project.forwork.api.domain.maillog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.maillog.infrastructure.enums.Result;

@Builder
@AllArgsConstructor
@Getter
public class MailLog {
    private final Long id;
    private final String email;
    private final String requestId;
    private final Long resumeId;
    private final Result result;
    private final String errorResponse;

    public static MailLog create(String email, String requestId, Long resumeId, Exception e){
        return MailLog.builder()
                .email(email)
                .requestId(requestId)
                .resumeId(resumeId)
                .result(Result.FAIL)
                .errorResponse(e.getMessage())
                .build();
    }

    public static MailLog create(String email, String requestId, Long resumeId){
        return MailLog.builder()
                .email(email)
                .requestId(requestId)
                .resumeId(resumeId)
                .result(Result.SUCCESS)
                .build();
    }
}
