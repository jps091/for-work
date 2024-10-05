package project.forwork.api.domain.transaction.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.TransactionErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.transaction.model.Transaction;
import project.forwork.api.domain.transaction.service.port.TransactionRepository;


@RequiredArgsConstructor
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionJpaRepository transactionJpaRepository;

    @Override
    public Transaction save(Transaction tx) {
        return transactionJpaRepository.save(TransactionEntity.from(tx)).toModel();
    }

    @Override
    public Transaction getByOrderIdWithTrow(Long orderId) {
        return transactionJpaRepository.findByOrderEntity_Id(orderId).map(TransactionEntity::toModel)
                .orElseThrow(() -> new ApiException(TransactionErrorCode.TX_NOT_FOUND));
    }

    @Override
    public Transaction getByOrderIdAndUserId(Long orderId, Long userId) {
        return transactionJpaRepository.findByOrderIdAndUserId(orderId, userId)
                .map(TransactionEntity::toModel)
                .orElseThrow(() -> new ApiException(TransactionErrorCode.TX_NOT_FOUND));
    }

    @Override
    public boolean existsByPaymentKey(String paymentKey) {
        return transactionJpaRepository.existsByPaymentKey(paymentKey);
    }
}
