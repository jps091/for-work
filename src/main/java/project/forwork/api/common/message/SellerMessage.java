package project.forwork.api.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerMessage {
    private String email;
    private String title;
    private String content;

    public static SellerMessage from(String email, String title, String content){
        return SellerMessage.builder()
                .email(email)
                .title(title)
                .content(content)
                .build();
    }
}
