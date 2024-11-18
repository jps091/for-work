package project.forwork.api.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TempPasswordMessage {
    private String email;
    private String title;
    private String content;

    public static TempPasswordMessage from(String email, String password){
        return TempPasswordMessage.builder()
                .email(email)
                .title("for-work 임시 비밀번호 발급")
                .content("임시 비밀번호 = " + password)
                .build();
    }
}