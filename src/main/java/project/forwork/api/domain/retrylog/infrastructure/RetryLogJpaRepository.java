package project.forwork.api.domain.retrylog.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RetryLogJpaRepository extends JpaRepository<RetryLogEntity, Long> {
}
