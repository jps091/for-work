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
import project.forwork.api.domain.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtTokenHelper implements TokenHelperIfs {

    public static final String ACCESS_TYPE = "ACCESS_TYPE";
    public static final String REFRESH_TYPE = "REFRESH_TYPE";
    public static final String TOKEN_TYPE = "tokenType";
    public static final String ROLE_TYPE = "roleType";
    private final ClockHolder clockHolder;

    @Value("${token.secret.key}")
    private String secretKey;

    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${token.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    @Override
    public Token issueAccessToken(Long userId) {
        return issueTokenFrom(userId, accessTokenPlusHour, ACCESS_TYPE);
    }

    @Override
    public Token issueRefreshToken(Long userId) {
        return issueTokenFrom(userId, refreshTokenPlusHour, REFRESH_TYPE);
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

    private Token issueTokenFrom(Long userId, Long tokenPlusHour, String tokenType) {
        LocalDateTime expiredTime = clockHolder.plusHours(tokenPlusHour);
        Date expiredAt = clockHolder.convertAbsoluteTime(expiredTime);

        Algorithm algo = Algorithm.HMAC256(secretKey.getBytes(StandardCharsets.UTF_8));

        String jwtToken = JWT.create()
                .withIssuer("for-work")
                .withSubject(userId.toString())
                .withClaim(TOKEN_TYPE, tokenType)
                .withExpiresAt(expiredAt)
                .sign(algo);

        return Token.builder()
                .token(jwtToken)
                .expiredAt(expiredTime)
                .build();
    }
}
