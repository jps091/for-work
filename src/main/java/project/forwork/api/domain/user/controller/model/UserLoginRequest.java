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
    @Schema(example = "seokin23@naver.com")
    private String email;

    @NotBlank
    @Schema(example = "123")
    private String password;
}
