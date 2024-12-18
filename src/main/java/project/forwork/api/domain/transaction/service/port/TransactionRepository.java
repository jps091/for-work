package project.forwork.api.domain.transaction.service.port;


import project.forwork.api.domain.transaction.infrastructure.enums.TransactionType;
import project.forwork.api.domain.transaction.model.Transaction;

public interface TransactionRepository {
    Transaction save(Transaction tx);
    Transaction getByRequestIdAndEmail(String requestId, String email);
    Transaction getByRequestIdAndEmail2(String requestId, String email);
}
