package project.forwork.api.domain.token.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
import project.forwork.api.domain.user.service.port.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenHelperIfs tokenHelper;
    private final ClockHolder clockHolder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public TokenResponse issueTokenResponse(User user) {

        refreshTokenRepository.findByUserId(user.getId())
                .ifPresentOrElse(refreshToken -> {
                    if(isTimeOutRefreshToken(refreshToken)){
                        reissueRefreshToken(refreshToken, user);
                    }
                }, () -> {
                    issueRefreshToken(user);
                });
        return createTokenResponse(user);
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        return createTokenResponse(user);
    }

    private boolean isTimeOutRefreshToken(RefreshToken refreshToken) {
        return refreshToken.getExpiredAt().isBefore(clockHolder.now());
    }

    private void issueRefreshToken(User user){
        Token token = tokenHelper.issueRefreshToken(user);
        refreshTokenRepository.save(RefreshToken.from(token, user.getId()));
    }

    private void reissueRefreshToken(RefreshToken refreshToken, User user){
        refreshTokenRepository.delete(refreshToken);
        issueRefreshToken(user);
    }

    private TokenResponse createTokenResponse(User user) {
        Token accessToken = tokenHelper.issueAccessToken(user);
        Token csrfToken = tokenHelper.issueCsrfToken(user);

        return TokenResponse.from(accessToken, csrfToken);
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
}
