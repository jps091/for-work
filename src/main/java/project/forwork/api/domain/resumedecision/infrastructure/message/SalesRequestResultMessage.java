package project.forwork.api.domain.resumedecision.infrastructure.message;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SalesRequestResultMessage {
    private String email;
    private String title;
    private String content;

    public static SalesRequestResultMessage approve(String email, String resumeUrl){
        return SalesRequestResultMessage.builder()
                .email(email)
                .title("for-work 이력서 판매 요청건 수락")
                .content("요청 하신 <" + resumeUrl + "> 해당 이력서 검토 결과 판매 승인 되었습니다")
                .build();
    }

    public static SalesRequestResultMessage deny(String email, String resumeUrl){
        return SalesRequestResultMessage.builder()
                .email(email)
                .title("for-work 이력서 판매 요청건 거절")
                .content("요청 하신 <" + resumeUrl + "> 해당 이력서 검토 결과 판매 거부 되었습니다.")
                .build();
    }
}
