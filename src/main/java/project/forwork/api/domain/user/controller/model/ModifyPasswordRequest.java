package project.forwork.api.domain.user.controller.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ModifyPasswordRequest {
    @NotBlank
    private String password;
}
