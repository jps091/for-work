package project.forwork.api.domain.token.model;

import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.service.port.ClockHolder;

import java.time.LocalDateTime;

@Getter
public class RefreshToken {
    private final Long id;
    private final String token;
    private final Long userId;
    private final LocalDateTime expiredAt;

    @Builder
    public RefreshToken(Long id, String token, Long userId, LocalDateTime expiredAt) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expiredAt = expiredAt;
    }

    public static RefreshToken from(Token token, Long userId){
        return RefreshToken.builder()
                .token(token.getToken())
                .userId(userId)
                .expiredAt(token.getExpiredAt())
                .build();
    }

    public RefreshToken initExpiredAt(RefreshToken refreshToken, ClockHolder clockHolder){
        return RefreshToken.builder()
                .id(refreshToken.getId())
                .token(refreshToken.getToken())
                .userId(refreshToken.getUserId())
                .expiredAt(clockHolder.now())
                .build();
    }
}
