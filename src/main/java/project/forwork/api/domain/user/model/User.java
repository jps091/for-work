package project.forwork.api.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.error.UserErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.common.service.port.ClockHolder;
import project.forwork.api.domain.user.controller.model.UserCreateRequest;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class User {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final UserStatus status;
    private final LocalDateTime lastLoginAt;

    public static User from(UserCreateRequest body){
        return User.builder()
                .email(body.getEmail())
                .name(body.getName())
                .password(body.getPassword())
                .status(UserStatus.USER)
                .build();
    }

    public User login(ClockHolder clockHolder, String password){
        if(status.equals(UserStatus.DELETE)){
            throw new ApiException(UserErrorCode.DELETE_USER);
        }

        if (!this.password.equals(password)) {
            throw new ApiException(UserErrorCode.PASSWORD_NOT_MATCH);
        }
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .status(status)
                .lastLoginAt(clockHolder.now())
                .build();
    }

    public User updatePassword(String password){
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .status(status)
                .lastLoginAt(lastLoginAt)
                .build();
    }

    public User initTemporaryPassword(String tempPassword){
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(tempPassword)
                .status(status)
                .lastLoginAt(lastLoginAt)
                .build();
    }

    public User delete(){
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .status(UserStatus.DELETE)
                .lastLoginAt(lastLoginAt)
                .build();
    }

    public boolean isPasswordMismatch(String password){
        return !Objects.equals(this.password, password);
    }
    public boolean isAdminMismatch(){
        return status != UserStatus.ADMIN;
    }
    public boolean isNameMismatch(String name){
        return !Objects.equals(this.name, name);
    }
}
