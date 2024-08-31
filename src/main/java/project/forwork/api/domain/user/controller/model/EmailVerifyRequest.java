package project.forwork.api.domain.user.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailVerifyRequest {
    @NotBlank(message = "인증 하실 email을 입력 해주세요.")
    @Email
    private String email;
    @NotBlank(message = "전송받은 코드를 입력 해주세요.")
    private String code;
}
