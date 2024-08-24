package project.forwork.api.domain.token.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class TokenResponse {

    private final String accessToken;
    private final LocalDateTime accessTokenExpiredAt;
    private final String csrfToken;
    private final LocalDateTime csrfTokenExpiredAt;

    @Builder
    public TokenResponse(String accessToken, LocalDateTime accessTokenExpiredAt, String csrfToken, LocalDateTime csrfTokenExpiredAt) {
        this.accessToken = accessToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.csrfToken = csrfToken;
        this.csrfTokenExpiredAt = csrfTokenExpiredAt;
    }

    public static TokenResponse from(Token accessToken, Token csrfToken) {
        Objects.requireNonNull(accessToken, "유효하지 않은 엑세스토큰입니다.");
        Objects.requireNonNull(csrfToken, "유효하지 않은 CSRF 토큰입니다.");

        return TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .csrfToken(csrfToken.getToken())
                .csrfTokenExpiredAt(csrfToken.getExpiredAt())
                .build();
    }
}
