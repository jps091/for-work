package project.forwork.api.domain.cartresume.controller.model;

import lombok.*;
import project.forwork.api.domain.cartresume.model.CartResume;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResumeResponse {

    private Long cartResumeId;
    private String title;
    private BigDecimal price;

    public static CartResumeResponse from(CartResume cartResume){
        return CartResumeResponse.builder()
                .cartResumeId(cartResume.getId())
                .title(cartResume.getTitle())
                .price(cartResume.getPrice())
                .build();
    }
}
