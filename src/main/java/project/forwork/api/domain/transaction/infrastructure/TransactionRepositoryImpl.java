package project.forwork.api.domain.transaction.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
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
    public boolean existsByOrderId(String orderId) {
        return transactionJpaRepository.existsByOrderId(orderId);
    }
}
