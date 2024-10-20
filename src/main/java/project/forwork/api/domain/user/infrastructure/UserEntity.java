package project.forwork.api.domain.user.infrastructure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;
import project.forwork.api.domain.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(length = 50, unique = true)
    private String email;

    @Column(length = 20) @NotNull
    private String password;

    @Column(length = 10) @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role") @NotNull
    private RoleType roleType;

    @Column(name = "last_login_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastLoginAt;

    public static UserEntity from(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.id = user.getId();
        userEntity.email = user.getEmail();
        userEntity.password = user.getPassword();
        userEntity.name = user.getName();
        userEntity.roleType = user.getRoleType();
        userEntity.lastLoginAt = user.getLastLoginAt();
        return userEntity;
    }

    public User toModel() {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .roleType(roleType)
                .lastLoginAt(lastLoginAt)
                .build();
    }
}
