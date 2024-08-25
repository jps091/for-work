package project.forwork.api.domain.token.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.token.model.RefreshToken;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public static RefreshTokenEntity from(RefreshToken refreshToken){
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.id = refreshToken.getId();
        refreshTokenEntity.token = refreshToken.getToken();
        refreshTokenEntity.userId = refreshToken.getUserId();
        refreshTokenEntity.expiredAt = refreshToken.getExpiredAt();
        return refreshTokenEntity;
    }

    public RefreshToken toModel(){
        return RefreshToken.builder()
                .id(id)
                .token(token)
                .userId(userId)
                .expiredAt(expiredAt)
                .build();
    }
}
