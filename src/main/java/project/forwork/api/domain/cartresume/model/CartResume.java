package project.forwork.api.domain.cartresume.model;

import lombok.Builder;
import lombok.Getter;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.resume.model.Resume;

import java.math.BigDecimal;
import java.util.Objects;


@Getter
public class CartResume {
    private final Long id;
    private final Cart cart;
    private final Resume resume;

    @Builder
    public CartResume(Long id, Cart cart, Resume resume) {
        this.id = id;
        this.cart = cart;
        this.resume = resume;
    }

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
        return resume.createSalePostTitle();
    }
}
