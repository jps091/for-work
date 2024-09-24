package project.forwork.api.domain.cartresume.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.resume.model.Resume;

import java.math.BigDecimal;
import java.util.Objects;


@Getter
@AllArgsConstructor
@Builder
public class CartResume {
    private final Long id;
    private final Cart cart;
    private final Resume resume;

    public static CartResume create(Cart cart, Resume resume){
        return CartResume.builder()
                .cart(cart)
                .resume(resume)
                .build();
    }

    public BigDecimal getPrice(){
        return resume.getPrice();
    }

    public String getTitle(){
        return resume.createSalesPostTitle();
    }
}
