package project.forwork.domain.cartresume.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.common.infrastructure.BaseTimeEntity;
import project.forwork.domain.cart.infrastructure.CartEntity;
import project.forwork.domain.resume.infrastructure.ResumeEntity;

@Entity
@Table(name = "cart_resumes")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartResumeEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cartEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private ResumeEntity resumeEntity;

    @Column(name = "is_selected", nullable = false)
    private boolean isSelected;
}
