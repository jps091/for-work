package project.forwork.api.domain.order.infrastructure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") @NotNull
    private UserEntity userEntity;

    @Column(length = 25, name = "request_id") @NotNull
    private String requestId;

    @Column(precision = 8, name = "total_amount") @NotNull
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status;

    @Column(name = "paid_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime paidAt;

    public static OrderEntity from(Order order){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.id = order.getId();
        orderEntity.userEntity = UserEntity.from(order.getUser());
        orderEntity.requestId = order.getRequestId();
        orderEntity.totalAmount = order.getTotalAmount();
        orderEntity.status = order.getStatus();
        orderEntity.paidAt = order.getPaidAt();
        return orderEntity;
    }

    public Order toModel(){
        return Order.builder()
                .id(id)
                .user(userEntity.toModel())
                .requestId(requestId)
                .totalAmount(totalAmount)
                .status(status)
                .paidAt(paidAt)
                .build();
    }
}
