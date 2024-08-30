package project.forwork.api.domain.user.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailVerifyRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String code;
}
