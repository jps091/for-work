package project.forwork.api.domain.token.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.forwork.api.common.error.TokenErrorCode;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.infrastructure.RedisStringUtilsImpl;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.token.helper.ifs.TokenHelperIfs;
import project.forwork.api.domain.token.model.*;
import project.forwork.api.domain.user.model.User;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenService {

    public static final String PREFIX_TOKEN_KEY = "refreshToken:userId:";
    private final TokenHelperIfs tokenHelper;
    private final ClockHolder clockHolder;
    private final RedisStringUtilsImpl redisStringUtils;
    public TokenResponse issueTokenResponse(User user) {

        Long userId = user.getId();
        String key = createKeyForm(userId);

        if(redisStringUtils.getData(key) != null){
            deleteRefreshToken(key);
            return createTokenResponse(userId);
        }

        return createTokenResponse(userId);
    }

    public TokenResponse reissueTokenResponse(String refreshTokenValue) {

        long userId = getUserIdByToken(refreshTokenValue);
        String storedToken = findRefreshTokenFrom(userId);
        long remainingTtl = redisStringUtils.getExpirationTime(createKeyForm(userId));

        if (isNotMatchTokenValue(refreshTokenValue, storedToken) || isTimeOutRefreshToken(remainingTtl)) {
            throw new ApiException(TokenErrorCode.EXPIRED_TOKEN);
        }

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
        Token refreshToken = issueRefreshToken(userId);


        return TokenResponse.from(accessToken, refreshToken);
    }

    private Token issueRefreshToken(Long userId){
        Token refreshToken = tokenHelper.issueRefreshToken(userId);
        String key = "refreshToken:userId:" + userId;

        redisStringUtils.setData(key, refreshToken.getToken(), refreshToken.getTtl());

        return refreshToken;
    }

    private void deleteRefreshToken(String key){
        redisStringUtils.deleteData(key);
    }

    private String findRefreshTokenFrom(long userId) {
        return redisStringUtils.getData(createKeyForm(userId));
    }

    private static String createKeyForm(Long userId) {
        return PREFIX_TOKEN_KEY + userId;
    }

    private boolean isTimeOutRefreshToken(long remainingTtl) {
        return remainingTtl <= clockHolder.millis();
    }

    private boolean isNotMatchTokenValue(String requestTokenValue, String targetTokenValue){
        return !Objects.equals(requestTokenValue, targetTokenValue);
    }
}
