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
import project.forwork.api.domain.wallet.model.Wallet;
import project.forwork.api.domain.wallet.service.port.WalletRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    public Transaction charge(CurrentUser currentUser, TransactionCreateRequest body){
        if(transactionRepository.existsByOrderTxId(body.getOrderTxId())){
            throw new ApiException(TransactionErrorCode.TX_AlREADY_CHARGE);
        }

        User user = userRepository.getByIdWithThrow(currentUser.getId());
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElse(Wallet.create(user));

        wallet = wallet.addBalance(body.getAmount(), clockHolder);
        wallet = walletRepository.save(wallet);

        return Transaction.createChargeTx(user, wallet, body, clockHolder);
    }

    public Transaction payment(CurrentUser currentUser, TransactionCreateRequest body){

        if(transactionRepository.existsByOrderTxId(body.getOrderTxId())){
            throw new ApiException(TransactionErrorCode.TX_AlREADY_PAYMENT);
        }

        User user = userRepository.getByIdWithThrow(currentUser.getId());
        Wallet wallet = walletRepository.getByUserIdWithThrow(user.getId());

        wallet = wallet.minBalance(body.getAmount(), clockHolder);
        wallet = walletRepository.save(wallet);

        return Transaction.createPaymentTx(user, wallet, body, clockHolder);
    }
}
