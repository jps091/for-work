package project.forwork.api.domain.maillog.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.domain.maillog.model.MailLog;
import project.forwork.api.domain.maillog.service.port.MailLogRepository;

@RequiredArgsConstructor
@Repository
public class MailLogRepositoryImpl implements MailLogRepository {

    private final MailLogJpaRepository mailLogJpaRepository;
    @Override
    public MailLog save(MailLog mailLog) {
        return mailLogJpaRepository.save(MailLogEntity.from(mailLog)).toModel();
    }
}
