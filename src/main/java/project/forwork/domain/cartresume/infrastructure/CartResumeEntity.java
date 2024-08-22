package project.forwork.domain.cartresume.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.forwork.common.infrastructure.BaseTimeEntity;
import project.forwork.domain.cart.infrastructure.CartEntity;
import project.forwork.domain.saleresume.infrastructure.SaleResumeEntity;

@Entity
@Table(name = "cart_resumes")
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartResumeEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_resume_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cartEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private SaleResumeEntity saleResumeEntity;

    @Column(name = "is_selected", nullable = false)
    private boolean isSelected;
}
