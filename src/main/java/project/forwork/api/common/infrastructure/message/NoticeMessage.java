package project.forwork.api.common.infrastructure.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeMessage {
    private String email;
    private String title;
    private String content;

    public static NoticeMessage from(String email){
        return NoticeMessage.builder()
                .email(email)
                .title("for-work 안내 사항")
                .content("회원 가입을 해주셔서 감사합니다. 해당 사이트는 참고 할만한 이력서를 판매 하는 사이트 입니다. \n" +
                        "이력서를 주문할 경우 자동구매 확정을 하지 않으면 30~60분이 경과 되면 자동 구매 확정 및 이력서 공유링크가 메일로 전송 됩니다.\n" +
                        "메일이 발송된 후 환불은 불가능 하니 참고 부탁드립니다.\n" +
                        "이력서를 판매하고 싶은 회원님은 사이트 상단에 이력서 판매 요청으로 양식을 작성해 주시면 관리자 검토후 가능합니다.\n" +
                        "기타 모든 문의 사항은 해당 메일로 보내주시면 감사하겠습니다.")
                .build();
    }
}
