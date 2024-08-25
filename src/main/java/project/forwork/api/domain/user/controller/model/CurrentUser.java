package project.forwork.api.domain.user.controller.model;

import lombok.Builder;
import lombok.Data;
import project.forwork.api.domain.user.infrastructure.enums.RoleType;

@Data
@Builder
public class CurrentUser {
    private Long id;
    private String name;
    private String email;
    private RoleType roleType;
}
