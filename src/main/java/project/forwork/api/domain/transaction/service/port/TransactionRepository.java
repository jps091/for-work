package project.forwork.api.domain.transaction.service.port;


import project.forwork.api.domain.transaction.model.Transaction;

import java.util.Optional;

public interface TransactionRepository {
    Transaction save(Transaction tx);
    Optional<Transaction> findByOrderId(Long orderId);
    boolean existsByOrderTxId(String orderTxId);
}
