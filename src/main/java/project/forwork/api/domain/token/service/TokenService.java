package project.forwork.api.domain.token.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.forwork.api.common.error.TokenErrorCode;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.token.helper.ifs.TokenHelperIfs;
import project.forwork.api.domain.token.model.RefreshToken;
import project.forwork.api.domain.token.model.Token;
import project.forwork.api.domain.token.model.TokenResponse;
import project.forwork.api.domain.token.service.port.RefreshTokenRepository;
import project.forwork.api.domain.user.model.User;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final TokenHelperIfs tokenHelper;
    private final ClockHolder clockHolder;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenResponse issueTokenResponse(User user) {

        Long userId = user.getId();

        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(refreshToken -> {
                    reissueRefreshToken(refreshToken, userId);
                }, () -> {
                    issueRefreshToken(userId);
                });
        return createTokenResponse(userId);
    }

    public TokenResponse reissueTokenResponse(String refreshTokenValue) {

        // 리프레쉬 토큰 값 자체로 데이터베이스에서 검색
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new ApiException(TokenErrorCode.INVALID_TOKEN));

        if (isTimeOutRefreshToken(refreshToken)) {
            throw new ApiException(TokenErrorCode.EXPIRED_TOKEN);
        }

        // 토큰에서 사용자 ID 추출
        Long userId = refreshToken.getUserId();

        return createTokenResponse(userId);
    }

    // JWT 토큰의 유효성 검사 후, 유효한 경우 사용자 ID 반환. 토큰이 유효하지 않거나, 사용자 ID가 없다면 예외를 던짐
    public Long validateAndGetUserId(String token) {
        Long userId = tokenHelper.validationTokenWithThrow(token);

        Objects.requireNonNull(userId, () -> {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        });

        return userId;
    }

    public Long getUserIdByToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return Long.parseLong(decodedJWT.getSubject());
    }

    private TokenResponse createTokenResponse(Long userId) {
        Token accessToken = tokenHelper.issueAccessToken(userId);
        Token refreshToken = tokenHelper.issueRefreshToken(userId);

        return TokenResponse.from(accessToken, refreshToken);
    }

    private void issueRefreshToken(Long userId){
        Token token = tokenHelper.issueRefreshToken(userId);
        refreshTokenRepository.save(RefreshToken.from(token, userId));
    }

    private void reissueRefreshToken(RefreshToken refreshToken, Long userId){
        RefreshToken oldRefreshToken = refreshToken.initExpiredAt(refreshToken, clockHolder);
        refreshTokenRepository.save(oldRefreshToken);

        refreshTokenRepository.delete(oldRefreshToken);
        issueRefreshToken(userId);
    }

    private boolean isTimeOutRefreshToken(RefreshToken refreshToken) {
        return refreshToken.getExpiredAt().isBefore(clockHolder.now());
    }
}
