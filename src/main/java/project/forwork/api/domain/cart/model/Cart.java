package project.forwork.api.domain.cart.model;

import lombok.Builder;
import lombok.Getter;
import project.forwork.api.common.error.ResumeErrorCode;
import project.forwork.api.domain.cart.infrastructure.enums.CartStatus;
import project.forwork.api.domain.resume.model.Resume;
import project.forwork.api.domain.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Cart {
    private final Long id;
    private final User user;
    private final CartStatus status;
    private final LocalDateTime modifiedAt;

    @Builder
    public Cart(Long id, User user, CartStatus status, LocalDateTime modifiedAt) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.modifiedAt = modifiedAt;
    }

    public static Cart create(User user){
        return Cart.builder()
                .user(user)
                .status(CartStatus.ACTIVE)
                .build();
    }
}
