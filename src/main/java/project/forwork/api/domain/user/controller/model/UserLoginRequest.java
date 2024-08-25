package project.forwork.api.domain.user.controller.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserLoginRequest {

    @NotBlank
    @Email
    @Schema(example = "123@naver.com")
    private String email;

    @NotBlank
    @Schema(example = "123")
    private String password;
}
