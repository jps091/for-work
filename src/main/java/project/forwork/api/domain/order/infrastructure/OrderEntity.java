package project.forwork.api.domain.order.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.api.domain.order.model.Order;
import project.forwork.api.domain.user.infrastructure.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter @Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(precision = 8, name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    public static OrderEntity from(Order order){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.id = order.getId();
        orderEntity.userEntity = UserEntity.from(order.getUser());
        orderEntity.totalPrice = order.getTotalPrice();
        orderEntity.status = order.getStatus();
        orderEntity.orderedAt = order.getOrderedAt();
        orderEntity.canceledAt = order.getCanceledAt();
        orderEntity.confirmedAt = order.getConfirmedAt();
        return orderEntity;
    }

    public Order toModel(){
        return Order.builder()
                .id(id)
                .user(userEntity.toModel())
                .totalPrice(totalPrice)
                .status(status)
                .orderedAt(orderedAt)
                .canceledAt(canceledAt)
                .confirmedAt(confirmedAt)
                .build();
    }
}
