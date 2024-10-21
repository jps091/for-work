package project.forwork.api.domain.user.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.token.model.TokenResponse;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private Long userId;
    private String accessToken;

    public static LoginResponse from(Long userId, String refreshToken){
        return LoginResponse.builder()
                .userId(userId)
                .accessToken(refreshToken)
                .build();
    }
}
