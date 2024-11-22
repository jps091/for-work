package project.forwork.api.domain.user.infrastructure.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.user.controller.model.InquiryRequest;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminInquiryMessage {
    private String email;
    private String title;
    private String content;

    public static AdminInquiryMessage from(String userEmail, InquiryRequest body){
        return AdminInquiryMessage.builder()
                .email("flow.jaepil@gmail.com")
                .title(body.getTitle())
                .content("문의 : " + userEmail +  " 유형 : " + body.getInquiryType().getDescription() + "\n" + body.getContent())
                .build();
    }
}
