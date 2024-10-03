package project.forwork.api.domain.wallet.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.WalletErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.wallet.model.Wallet;
import project.forwork.api.domain.wallet.service.port.WalletRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {

    private final WalletJpaRepository walletJpaRepository;
    @Override
    public Wallet save(Wallet wallet) {
        return walletJpaRepository.save(WalletEntity.from(wallet)).toModel();
    }

    @Override
    public Optional<Wallet> findByUserId(Long userId) {
        return walletJpaRepository.findByUserEntity_Id(userId).map(WalletEntity::toModel);
    }

    @Override
    public Wallet getByUserIdWithThrow(Long userId) {
        return findByUserId(userId).orElseThrow(() -> new ApiException(WalletErrorCode.WALLET_NOT_EXIST));
    }

    @Override
    public Wallet getByWalletIdWithThrow(Long walletId) {
        return walletJpaRepository.findById(walletId).map(WalletEntity::toModel)
                .orElseThrow(() -> new ApiException(WalletErrorCode.WALLET_NOT_EXIST));
    }
}
