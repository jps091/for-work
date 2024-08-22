package project.forwork.domain.saleresumedecision.infrastructure;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.common.infrastructure.BaseTimeEntity;
import project.forwork.domain.saleresumedecision.infrastructure.enums.DecisionStatus;
import project.forwork.domain.saleresume.infrastructure.SaleResumeEntity;
import project.forwork.domain.user.infrastructure.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "sale_resume_decisions")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleResumeDecisionEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_resume_decision_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity sellerEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private UserEntity adminEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_resume_id", nullable = false)
    private SaleResumeEntity saleResumeEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DecisionStatus decisionStatus;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;
}
