package project.forwork.api.domain.user.controller.model;

import lombok.*;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;
import project.forwork.api.domain.user.model.User;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private UserStatus status;

    public static UserResponse from(User user){
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .status(user.getStatus())
                .build();
    }
}
