package project.forwork.api.domain.order.infrastructure.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellingMessage {
    private String email;
    private String title;
    private String content;

    public static SellingMessage from(String email, String title, String content){
        return SellingMessage.builder()
                .email(email)
                .title(title)
                .content(content)
                .build();
    }
}
