package project.forwork.api.domain.user.model;

import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.user.controller.model.UserCreateRequest;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;

@Getter
public class User {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final RoleType roleType;
    private final Long lastLoginAt;

    @Builder
    public User(Long id, String name, String email, String password, RoleType roleType, Long lastLoginAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleType = roleType;
        this.lastLoginAt = lastLoginAt;
    }

    public static User from(UserCreateRequest request){
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .roleType(RoleType.USER)
                .build();
    }


}
