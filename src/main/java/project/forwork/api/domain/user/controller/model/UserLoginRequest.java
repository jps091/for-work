package project.forwork.api.domain.user.controller.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    @NotBlank
    @Email
    @Schema(example = "admin@test.com")
    private String email;

    @NotBlank
    @Schema(example = "admin1234@")
    private String password;
}
