package project.forwork.api.mock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import project.forwork.api.common.error.CartErrorCode;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.common.exception.ApiException;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.cart.service.port.CartRepository;
import project.forwork.api.domain.resume.controller.model.ResumeResponse;
import project.forwork.api.domain.resume.infrastructure.enums.ResumeStatus;
import project.forwork.api.domain.resume.infrastructure.querydsl.ResumeSearchCond;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.resume.service.port.ResumeRepository;
import project.forwork.api.domain.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeCartRepository implements CartRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<Cart> data = new ArrayList<>();


    @Override
    public Cart save(Cart cart) {
        if(cart.getId() == null || cart.getId() == 0){
            Cart newCart = Cart.builder()
                    .id(id.incrementAndGet())
                    .user(cart.getUser())
                    .status(cart.getStatus())
                    .modifiedAt(cart.getModifiedAt())
                    .build();
            data.add(newCart);
            return newCart;
        }else{
            data.removeIf(c -> Objects.equals(c.getId(), cart.getId()));
            data.add(cart);
            return cart;
        }
    }

    @Override
    public Cart getByUserIdWithThrow(Long userId) {
        return data.stream().filter(c -> Objects.equals(c.getUser().getId(), userId)).findAny()
                .orElseThrow(() -> new ApiException(CartErrorCode.CART_NOT_FOUND, userId));
    }
}