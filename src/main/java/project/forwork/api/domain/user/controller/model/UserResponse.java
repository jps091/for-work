package project.forwork.api.domain.user.controller.model;

import lombok.*;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String name;
    private String email;
    private RoleType roleType;

    public static UserResponse from(User user){
        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .roleType(user.getRoleType())
                .build();
    }
}
