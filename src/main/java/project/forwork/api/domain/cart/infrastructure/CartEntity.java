package project.forwork.api.domain.cart.infrastructure;

import jakarta.persistence.*;
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
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private CartStatus status;

    @Column(name = "total_price", precision = 8, scale = 0, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    public static CartEntity from(Cart cart){
        CartEntity cartEntity = new CartEntity();
        cartEntity.id = cart.getId();
        cartEntity.userEntity = UserEntity.from(cart.getUser());
        cartEntity.status = cart.getStatus();
        return cartEntity;
    }

    public Cart toModel(){
        return Cart.builder()
                .id(id)
                .user(userEntity.toModel())
                .status(status)
                .modifiedAt(getModifiedAt())
                .build();
    }
}
