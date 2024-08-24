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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static project.forwork.api.domain.token.helper.JwtTokenHelper.ROLE_TYPE;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenHelperIfs tokenHelper;
    private final ClockHolder clockHolder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public TokenResponse issueToken(User user) {

        return Optional.ofNullable(user)
                .map(u -> {
                    Token token = createRefreshToken(u);
                    refreshTokenRepository.save(RefreshToken.from(token, u.getId()));
                    return createTokenResponse(u);
                })
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public TokenResponse reissueToken(String refreshTokenValue) {

        // 리프레쉬 토큰 값 자체로 데이터베이스에서 검색
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new ApiException(TokenErrorCode.INVALID_TOKEN));

        if (refreshToken.getExpiredAt().isBefore(clockHolder.now())) {
            throw new ApiException(TokenErrorCode.EXPIRED_TOKEN);
        }

        // 토큰에서 사용자 ID 추출
        Long userId = refreshToken.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        return createTokenResponse(user);
    }

    private TokenResponse createTokenResponse(User user) {
        Token accessToken = tokenHelper.issueAccessToken(user);
        Token csrfToken = tokenHelper.issueCsrfToken(user);

        return TokenResponse.from(accessToken, csrfToken);
    }

    // JWT 토큰의 유효성 검사 후, 유효한 경우 사용자 ID 반환. 토큰이 유효하지 않거나, 사용자 ID가 없다면 예외를 던짐
    public Long validationToken(String token) {
        Long userId = tokenHelper.validationTokenWithThrow(token);

        Objects.requireNonNull(userId, () -> {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        });

        return userId;
    }

    private Token createRefreshToken(User user) {
        return tokenHelper.issueRefreshToken(user);
    }

    public String getRoleByToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim(ROLE_TYPE).asString();
    }

    public Long getUserIdByToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return Long.parseLong(decodedJWT.getSubject());
    }
}
