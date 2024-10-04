package project.forwork.api.domain.transaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.TransactionErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.transaction.controller.model.TransactionCreateRequest;
import project.forwork.api.domain.transaction.model.Transaction;
import project.forwork.api.domain.transaction.service.port.TransactionRepository;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    public Transaction charge(CurrentUser currentUser, TransactionCreateRequest body){
        if(transactionRepository.existsByOrderId(body.getOrderTxId())){
            throw new ApiException(TransactionErrorCode.TX_AlREADY_CHARGE);
        }

        User user = userRepository.getByIdWithThrow(currentUser.getId());

        return Transaction.createChargeTx(user, body, clockHolder);
    }

    public Transaction payment(CurrentUser currentUser, TransactionCreateRequest body){

        if(transactionRepository.existsByOrderId(body.getOrderTxId())){
            throw new ApiException(TransactionErrorCode.TX_AlREADY_PAYMENT);
        }

        User user = userRepository.getByIdWithThrow(currentUser.getId());


        return Transaction.createPaymentTx(user, body, clockHolder);
    }
}
