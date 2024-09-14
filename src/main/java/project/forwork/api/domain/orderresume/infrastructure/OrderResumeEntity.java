package project.forwork.api.domain.orderresume.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.api.common.infrastructure.BaseTimeEntity;
import project.forwork.api.domain.order.infrastructure.OrderEntity;
import project.forwork.api.domain.orderresume.infrastructure.enums.OrderResumeStatus;
import project.forwork.api.domain.orderresume.model.OrderResume;
import project.forwork.api.domain.resume.infrastructure.ResumeEntity;

import java.math.BigDecimal;

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
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private ResumeEntity resumeEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderResumeStatus status;

    public static OrderResumeEntity from(OrderResume orderResume){
        OrderResumeEntity orderResumeEntity = new OrderResumeEntity();
        orderResumeEntity.id = orderResume.getId();
        orderResumeEntity.orderEntity = OrderEntity.from(orderResume.getOrder());
        orderResumeEntity.resumeEntity = ResumeEntity.from(orderResume.getResume());
        orderResumeEntity.status = orderResume.getStatus();
        return orderResumeEntity;
    }

    public OrderResume toModel(){
        return OrderResume.builder()
                .id(id)
                .order(orderEntity.toModel())
                .resume(resumeEntity.toModel())
                .status(status)
                .build();
    }
}
