package project.forwork.api.domain.token.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TokenResponse {

    private final String accessToken;
    private final long accessTokenExpiredAt;
    private final String refreshToken;
    private final long refreshTokenExpiredAt;

    @Builder
    public TokenResponse(String accessToken, long accessTokenExpiredAt, String refreshToken, long refreshTokenExpiredAt) {
        this.accessToken = accessToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
    }
    public static TokenResponse from(Token accessToken, Token refreshToken) {
        Objects.requireNonNull(accessToken, "유효하지 않은 Access 토큰입니다.");
        Objects.requireNonNull(refreshToken, "유효하지 않은 Refresh 토큰입니다.");

        return TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getTtl())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getTtl())
                .build();
    }
}
