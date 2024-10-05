package project.forwork.api.domain.retryrequest.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RetryLogJpaRepository extends JpaRepository<RetryLogEntity, Long> {
}
