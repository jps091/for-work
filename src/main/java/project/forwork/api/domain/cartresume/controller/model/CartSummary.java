package project.forwork.api.domain.cartresume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartSummary {

    BigDecimal totalPrice;
    int totalQuantity;
}
