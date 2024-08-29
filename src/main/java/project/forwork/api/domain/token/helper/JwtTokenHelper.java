package project.forwork.api.domain.token.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.forwork.api.common.error.TokenErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.token.helper.ifs.TokenHelperIfs;
import project.forwork.api.domain.token.model.Token;

import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtTokenHelper implements TokenHelperIfs {

    public static final String ACCESS_TYPE = "ACCESS_TYPE";
    public static final String REFRESH_TYPE = "REFRESH_TYPE";
    public static final String TOKEN_TYPE = "tokenType";
    private final ClockHolder clockHolder;

    @Value("${token.secret.key}")
    private String secretKey;

    @Value("${token.access-token.plus-minutes}")
    private long accessTokenPlusMinutes;

    @Value("${token.refresh-token.plus-minutes}")
    private long refreshTokenPlusMinutes;

    @Override
    public Token issueAccessToken(Long userId) {
        return issueTokenFrom(userId, accessTokenPlusMinutes, ACCESS_TYPE);
    }

    @Override
    public Token issueRefreshToken(Long userId) {
        return issueTokenFrom(userId, refreshTokenPlusMinutes, REFRESH_TYPE);
    }

    @Override
    public Long validationTokenWithThrow(String token) {
        Algorithm algo = Algorithm.HMAC256(secretKey.getBytes(StandardCharsets.UTF_8));

        JWTVerifier verifier = JWT.require(algo).build();

        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return Long.parseLong(decodedJWT.getSubject());
        } catch (TokenExpiredException e) {
            throw new ApiException(TokenErrorCode.EXPIRED_TOKEN, e);
        } catch (JWTVerificationException e) {
            throw new ApiException(TokenErrorCode.INVALID_TOKEN, e);
        }
    }

    private Token issueTokenFrom(Long userId, long tokenPlusMinutes, String tokenType) {
        long ttlSeconds = clockHolder.convertSecondsFrom(tokenPlusMinutes);
        Date expiredAt = clockHolder.convertExpiredDateFrom(ttlSeconds);

        Algorithm algo = Algorithm.HMAC256(secretKey.getBytes(StandardCharsets.UTF_8));

        String jwtToken = JWT.create()
                .withIssuer("for-work")
                .withSubject(userId.toString())
                .withClaim(TOKEN_TYPE, tokenType)
                .withExpiresAt(expiredAt)
                .sign(algo);

        return Token.builder()
                .token(jwtToken)
                .ttl(ttlSeconds)
                .build();
    }
}
