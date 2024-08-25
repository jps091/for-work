package project.forwork.api.domain.user.controller.model;

import lombok.Builder;
import lombok.Data;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;

@Builder
@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private RoleType roleType;

    public static UserResponse from(User user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .roleType(user.getRoleType())
                .build();
    }
}