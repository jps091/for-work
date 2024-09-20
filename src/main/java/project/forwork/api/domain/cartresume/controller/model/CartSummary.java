package project.forwork.api.domain.cartresume.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartSummary {

    BigDecimal totalPrice;
    int totalQuantity;
}
