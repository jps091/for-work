package project.forwork.api.domain.cartresume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.forwork.api.domain.cartresume.model.CartResume;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResumeDetailResponse {

    int totalQuantity;
    BigDecimal totalPrice;
    List<CartResumeResponse> cartResumeResponses;
}
