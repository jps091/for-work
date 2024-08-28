package project.forwork.api.domain.token.model;

import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.service.port.ClockHolder;


@Getter
public class RefreshToken {
    private final String authId;
    private final String token;
    private final Long userId;
    private final long ttl;

    @Builder
    public RefreshToken(String authId, String token, Long userId, long ttl) {
        this.authId = authId;
        this.token = token;
        this.userId = userId;
        this.ttl = ttl;
    }
    public static RefreshToken from(Token token, Long userId){
        return RefreshToken.builder()
                .token(token.getToken())
                .userId(userId)
                .ttl(token.getTtl())
                .build();
    }

    public RefreshToken initExpiredAt(RefreshToken refreshToken, ClockHolder clockHolder){
        return RefreshToken.builder()
                .authId(refreshToken.getAuthId())
                .token(refreshToken.getToken())
                .userId(refreshToken.getUserId())
                .ttl(clockHolder.millis())
                .build();
    }
}
