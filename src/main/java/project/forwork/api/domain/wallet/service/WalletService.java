package project.forwork.api.domain.wallet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.WalletErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.user.model.User;
import project.forwork.api.domain.user.service.port.UserRepository;
import project.forwork.api.domain.wallet.controller.model.BalanceUpdateRequest;
import project.forwork.api.domain.wallet.model.Wallet;
import project.forwork.api.domain.wallet.service.port.WalletRepository;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletService {

    public static final BigDecimal BALANCE_LIMIT = new BigDecimal(300_000L);
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public Wallet createWallet(CurrentUser currentUser){
        User user = userRepository.getByIdWithThrow(currentUser.getId());

        if(walletRepository.findByUserId(currentUser.getId()).isPresent()){
            throw new ApiException(WalletErrorCode.WALLET_EXIST);
        }

        Wallet wallet = Wallet.create(user);
        wallet = walletRepository.save(wallet);
        return wallet;
    }
}
