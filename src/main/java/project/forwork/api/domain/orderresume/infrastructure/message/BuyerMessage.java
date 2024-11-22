package project.forwork.api.domain.orderresume.infrastructure.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.orderresume.controller.model.OrderResumePurchaseInfo;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyerMessage {
    private String email;
    private String title;
    private String content;
    private Long orderId;
    private Long resumeId;

    public static BuyerMessage from(String title, String content, OrderResumePurchaseInfo info){
        return BuyerMessage.builder()
                .email(info.getBuyerEmail())
                .title(title)
                .content(content)
                .orderId(info.getOrderId())
                .resumeId(info.getResumeId())
                .build();
    }
}
