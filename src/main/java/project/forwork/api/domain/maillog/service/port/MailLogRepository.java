package project.forwork.api.domain.maillog.service.port;

import project.forwork.api.domain.maillog.model.MailLog;

public interface MailLogRepository {
    MailLog save(MailLog mailLog);
}
