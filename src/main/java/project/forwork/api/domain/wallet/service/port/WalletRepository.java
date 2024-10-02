package project.forwork.api.domain.wallet.service.port;

import project.forwork.api.domain.wallet.model.Wallet;

import java.util.Optional;

public interface WalletRepository {
    Wallet save(Wallet wallet);
    Optional<Wallet> findByUserId(Long userId);
    Wallet getByUserIdWithThrow(Long userId);
    Wallet getByWalletIdWithThrow(Long walletId);
}
