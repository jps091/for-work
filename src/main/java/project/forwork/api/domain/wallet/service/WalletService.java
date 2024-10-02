package project.forwork.api.domain.wallet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.domain.CurrentUser;
import project.forwork.api.common.error.WalletErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
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

    public Wallet createWallet(CurrentUser currentUser){

        if(isWalletExist(currentUser)){
            throw new ApiException(WalletErrorCode.WALLET_EXIST);
        }

        Wallet wallet = Wallet.create(currentUser);
        wallet = walletRepository.save(wallet);
        return wallet;
    }

    @Transactional(readOnly = true)
    public boolean isWalletExist(CurrentUser currentUser) {
        return walletRepository.findByUserId(currentUser.getId()).isPresent();
    }
}
