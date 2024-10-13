package project.forwork.api.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.user.controller.model.UserCreateRequest;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;

import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class User {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final RoleType roleType;
    private final Long lastLoginAt;

    public static User from(UserCreateRequest body){
        return User.builder()
                .email(body.getEmail())
                .name(body.getName())
                .password(body.getPassword())
                .roleType(RoleType.USER)
                .build();
    }

    public User login(ClockHolder clockHolder, String password){
        if (!this.password.equals(password)) {
            throw new ApiException(UserErrorCode.PASSWORD_NOT_MATCH);
        }
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .roleType(roleType)
                .lastLoginAt(clockHolder.millis())
                .build();
    }

    public User updatePassword(String password){
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .roleType(roleType)
                .lastLoginAt(lastLoginAt)
                .build();
    }

    public User initTemporaryPassword(String tempPassword){
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(tempPassword)
                .roleType(roleType)
                .lastLoginAt(lastLoginAt)
                .build();
    }

    public boolean isPasswordMismatch(String password){
        return !Objects.equals(this.password, password);
    }
    public boolean isAdminMismatch(){
        return roleType != RoleType.ADMIN;
    }
    public boolean isNameMismatch(String name){
        return !Objects.equals(this.name, name);
    }
}
