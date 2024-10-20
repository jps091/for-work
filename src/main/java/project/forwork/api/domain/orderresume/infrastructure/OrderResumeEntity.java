package project.forwork.api.domain.orderresume.infrastructure;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.order.infrastructure.OrderEntity;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_resumes")
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResumeEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_resume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") @NotNull
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id") @NotNull
    private ResumeEntity resumeEntity;

    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderResumeStatus status;

    @Column(name = "sent_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime sentAt;

    @Column(name = "canceled_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime canceledAt;

    public static OrderResumeEntity from(OrderResume orderResume){
        OrderResumeEntity orderResumeEntity = new OrderResumeEntity();
        orderResumeEntity.id = orderResume.getId();
        orderResumeEntity.orderEntity = OrderEntity.from(orderResume.getOrder());
        orderResumeEntity.resumeEntity = ResumeEntity.from(orderResume.getResume());
        orderResumeEntity.status = orderResume.getStatus();
        orderResumeEntity.sentAt = orderResume.getSentAt();
        orderResumeEntity.canceledAt = orderResume.getCanceledAt();
        return orderResumeEntity;
    }

    public OrderResume toModel(){
        return OrderResume.builder()
                .id(id)
                .order(orderEntity.toModel())
                .resume(resumeEntity.toModel())
                .status(status)
                .sentAt(sentAt)
                .canceledAt(canceledAt)
                .build();
    }
}
