package project.forwork.api.domain.transaction.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByRequestIdAndUserEmail(String requestId, String userEmail);
}
