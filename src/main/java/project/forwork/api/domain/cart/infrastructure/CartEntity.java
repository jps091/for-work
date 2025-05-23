package project.forwork.api.domain.cart.infrastructure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.cart.infrastructure.enums.CartStatus;
import project.forwork.api.domain.cart.model.Cart;
import project.forwork.api.domain.user.infrastructure.UserEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "carts")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status") @NotNull
    private CartStatus status;

    public static CartEntity from(Cart cart){
        CartEntity cartEntity = new CartEntity();
        cartEntity.id = cart.getId();
        cartEntity.userId = cart.getUserId();
        cartEntity.status = cart.getStatus();
        return cartEntity;
    }

    public Cart toModel(){
        return Cart.builder()
                .id(id)
                .userId(userId)
                .status(status)
                .build();
    }
}
