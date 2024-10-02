package project.forwork.api.domain.wallet.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletJpaRepository extends JpaRepository<WalletEntity, Long> {
    Optional<WalletEntity> findByUserEntity_Id(Long userId);
}
