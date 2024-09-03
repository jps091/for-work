package project.forwork.api.common.domain;

import lombok.*;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;

@Builder
@AllArgsConstructor
@Getter
public class CurrentUser {
    private final Long id;
    private final String name;
    private final String email;
    private final RoleType roleType;

    public boolean isAdminMismatch(){
        return roleType != RoleType.ADMIN;
    }
}
