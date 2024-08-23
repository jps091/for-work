package project.forwork.api.domain.payment.infrastructure;

import project.forwork.api.common.infrastructure.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import project.forwork.api.domain.order.infrastructure.OrderEntity;
import project.forwork.api.domain.payment.infrastructure.enums.PaymentStatus;

import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_method")
@Getter @Setter
public abstract class PaymentEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity orderEntity;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus paymentStatus;

    // 공통 메서드 (추상 메서드도 가능)
    public abstract void processPayment();
}
