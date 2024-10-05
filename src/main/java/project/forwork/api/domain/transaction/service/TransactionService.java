package project.forwork.api.domain.transaction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.error.TransactionErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.order.service.port.OrderRepository;
import project.forwork.api.domain.transaction.controller.model.TransactionCreateRequest;
import project.forwork.api.domain.transaction.model.Transaction;
import project.forwork.api.domain.transaction.service.port.TransactionRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Transaction createTransaction(TransactionCreateRequest body){
        User user = userRepository.getByIdWithThrow(body.getUserId());
        Order order = orderRepository.getByIdWithThrow(body.getOrderId());

        Transaction transaction = Transaction.registerPgPayment(user, order, body);
        return transactionRepository.save(transaction);
    }

    public Transaction getByOrderIdAndUserId(Long orderId, Long userId){
        return transactionRepository.getByOrderIdAndUserId(orderId, userId);
    }
}
