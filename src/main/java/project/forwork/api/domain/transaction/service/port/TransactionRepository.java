package project.forwork.api.domain.transaction.service.port;


import project.forwork.api.domain.transaction.model.Transaction;

public interface TransactionRepository {
    Transaction save(Transaction tx);
    Transaction getByOrderIdWithTrow(Long orderId);
    Transaction getByOrderIdAndUserId(Long orderId, Long userId);
    boolean existsByPaymentKey(String orderId);
}
