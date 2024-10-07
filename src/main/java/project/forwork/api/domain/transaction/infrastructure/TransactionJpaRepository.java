package project.forwork.api.domain.transaction.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType;

import java.util.Optional;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long> {

    boolean existsByPaymentKey(String paymentKey);
    Optional<TransactionEntity> findByOrderEntity_Id(Long orderId);

    @Query("select t from TransactionEntity t" +
            " join fetch t.orderEntity o" +
            " join fetch t.userEntity  u" +
            " where t.transactionType = :type and o.id = :orderId and u.id = :userId")
    Optional<TransactionEntity> findByOrderIdAndUserId(@Param("type") TransactionType type, @Param("orderId") Long orderId, @Param("userId") Long userId);
}
