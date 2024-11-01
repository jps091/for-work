package project.forwork.api.common.domain;

import lombok.*;
import project.forwork.api.domain.user.infrastructure.enums.UserStatus;

@Builder
@AllArgsConstructor
@Getter
public class CurrentUser {
    private final Long id;
    private final String name;
    private final String email;
    private final UserStatus status;

    public boolean isAdminMismatch(){
        return status != UserStatus.ADMIN;
    }
}
