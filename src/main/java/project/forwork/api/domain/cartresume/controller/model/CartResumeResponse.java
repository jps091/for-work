package project.forwork.api.domain.cartresume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.cartresume.model.CartResume;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResumeResponse {

    private Long id;
    private String title;
    private BigDecimal price;

    public static CartResumeResponse from(CartResume cartResume){
        return CartResumeResponse.builder()
                .id(cartResume.getId())
                .title(cartResume.getTitle())
                .price(cartResume.getPrice())
                .build();
    }
}
