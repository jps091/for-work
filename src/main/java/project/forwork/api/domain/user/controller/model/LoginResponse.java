package project.forwork.api.domain.user.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.token.model.TokenResponse;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;
import project.forwork.api.domain.user.model.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private Long userId;
    private UserStatus status;
    private String accessToken;

    public static LoginResponse from(User user){
        return LoginResponse.builder()
                .userId(user.getId())
                .status(user.getStatus())
                .build();
    }
}
