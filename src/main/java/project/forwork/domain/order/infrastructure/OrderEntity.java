package project.forwork.domain.order.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.common.infrastructure.BaseTimeEntity;
import project.forwork.domain.order.infrastructure.enums.OrderStatus;
import project.forwork.domain.user.infrastructure.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter @Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    @Column(name = "confirm_date")
    private LocalDateTime confirmDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; //[ORDER, CANCEL, CONFIRM]
}
