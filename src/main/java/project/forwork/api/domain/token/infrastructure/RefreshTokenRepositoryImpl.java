package project.forwork.api.domain.token.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.forwork.api.common.error.TokenErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.token.model.RefreshToken;
import project.forwork.api.domain.token.service.port.RefreshTokenRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.from(refreshToken);
        return refreshTokenJpaRepository.save(refreshTokenEntity).toModel();
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenJpaRepository.findByToken(token).map(RefreshTokenEntity::toModel);
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        return refreshTokenJpaRepository.findByUserId(userId).map(RefreshTokenEntity::toModel);
    }

    @Override
    public RefreshToken getByUserId(Long userId) {
        return findByUserId(userId)
                .orElseThrow(() -> new ApiException(TokenErrorCode.EXCEPTION_TOKEN));
    }

    @Override
    public void delete(RefreshToken refreshToken) {
        refreshTokenJpaRepository.delete(RefreshTokenEntity.from(refreshToken));
    }
}
