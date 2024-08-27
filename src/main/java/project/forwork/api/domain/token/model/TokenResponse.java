package project.forwork.api.domain.token.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class TokenResponse {

    private final String accessToken;
    private final LocalDateTime accessTokenExpiredAt;
    private final String refreshToken;
    private final LocalDateTime refreshTokenExpiredAt;

    @Builder
    public TokenResponse(String accessToken, LocalDateTime accessTokenExpiredAt, String refreshToken, LocalDateTime refreshTokenExpiredAt) {
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
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getExpiredAt())
                .build();
    }
}
