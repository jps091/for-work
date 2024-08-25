package project.forwork.api.domain.token.service.port;

import project.forwork.api.domain.token.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(Long userId);

    RefreshToken getByUserId(Long userId);

    void delete(RefreshToken refreshToken);
}
