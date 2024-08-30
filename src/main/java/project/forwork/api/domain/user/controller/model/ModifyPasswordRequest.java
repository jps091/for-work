package project.forwork.api.domain.user.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModifyPasswordRequest {
    @NotBlank
    private String password;
}
